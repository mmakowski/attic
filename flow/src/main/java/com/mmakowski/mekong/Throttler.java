package com.mmakowski.mekong;

import java.util.Date;

/**
 * Throttles messages so that the rate does not exceed specified number of messages per second. 
 * 
 * @param <TMsg> the type of messages to be throttled
 */
public class Throttler<TMsg> implements Processor<TMsg> {
	private final ProducerDelegate<TMsg> delegate = new ProducerDelegate<TMsg>();
	private final double messagesPerSecond; 
	private long lastMessageTime;
	private TMsg currentMessage;
	private boolean delayedProducerThreadRunning;
	private final Object producerThreadLock = new Object(); 
	
	public Throttler(double messagesPerSecond) {
		this.messagesPerSecond = messagesPerSecond;
	}
	
	@Override
	public void addConsumer(Consumer<TMsg> consumer) {
		delegate.addConsumer(consumer);
	}

	@Override
	public void consume(TMsg message) {
		synchronized (producerThreadLock) {
			final long timeToNextMessage = lastMessageTime + (long) (1000 / messagesPerSecond) - new Date().getTime();
			if (timeToNextMessage <= 0 && !delayedProducerThreadRunning) produce(message);
			else produceAfterDelay(message, timeToNextMessage);
		}
	}

	private void produceAfterDelay(TMsg message, long delay) {
		currentMessage = message;
		if (!delayedProducerThreadRunning) {
			startDelayedProducerThread(delay);
		}
	}

	private void startDelayedProducerThread(final long delay) {
		Thread delayedProducerThread = new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
					return;
				}
				synchronized (producerThreadLock) {
					produce(currentMessage);
					delayedProducerThreadRunning = false;
				}
			}
		};
		delayedProducerThread.start();
		delayedProducerThreadRunning = true;
	}
	
	private void produce(TMsg message) {
		delegate.produce(message);
		lastMessageTime = new Date().getTime();
	}
}
