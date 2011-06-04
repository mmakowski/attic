package com.mmakowski.mekong;

public interface Consumer<TMsg> {
	void consume(TMsg message);
}
