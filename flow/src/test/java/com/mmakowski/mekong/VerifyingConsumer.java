package com.mmakowski.mekong;

import java.util.LinkedList;
import java.util.List;

import com.mmakowski.mekong.Consumer;

public abstract class VerifyingConsumer<TMsg> implements Consumer<TMsg> {
	protected final List<TMsg> receivedMessages = new LinkedList<TMsg>();
	
	@Override
	public synchronized void consume(TMsg message) {
		receivedMessages.add(message);
	}
	
	public abstract void verify();
}
