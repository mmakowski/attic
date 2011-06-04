package com.mmakowski.scratch.rtp.reuters;

import com.mmakowski.mekong.Consumer;
import com.mmakowski.mekong.Producer;
import com.mmakowski.mekong.ProducerDelegate;

public class ReutersSubscriber implements Producer<ReutersMessage> {
	private final ProducerDelegate<ReutersMessage> delegate = new ProducerDelegate<ReutersMessage>();

	@Override
	public void addConsumer(Consumer<ReutersMessage> consumer) {
		delegate.addConsumer(consumer);
	}
	
	public void run() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			delegate.produce(ReutersMessage.of("RIC", "JPY=" + i, 
											   "BID", String.valueOf(213 + i), 
											   "ASK", String.valueOf(232 + i)));
			Thread.sleep(100);
		}
	}
}
