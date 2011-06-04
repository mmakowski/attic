package com.mmakowski.mekong;

public interface Producer<TMsg> {
	void addConsumer(Consumer<TMsg> consumer);
}
