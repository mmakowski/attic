package com.mmakowski.mekong;

import java.util.LinkedList;
import java.util.List;

/**
 * Implements standard producer functionality. Classes implementing Producer interface
 * can instantiate an object of this class and then forward all calls to Producer
 * interface methods to that object.
 * 
 * Note that this implementation provides the message to all consumers sequentially, i.e. a 
 * consumer needs to finish its processing of a message before the next consumer can start 
 * processing that message. 
 * 
 * @param <TMsg> type of messages produced
 */
public class ProducerDelegate<TMsg> implements Producer<TMsg> {
	private final List<Consumer<TMsg>> consumers = new LinkedList<Consumer<TMsg>>();
	
	@Override
	public void addConsumer(Consumer<TMsg> consumer) {
		consumers.add(consumer);
	}
	
	public void produce(TMsg message) {
		for (Consumer<TMsg> consumer : consumers) {
			consumer.consume(message);
		}
	}
}
