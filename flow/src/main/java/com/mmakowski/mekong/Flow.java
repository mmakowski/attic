package com.mmakowski.mekong;

/**
 * Convenience functions for connecting components of a flow.
 */
public final class Flow {
	public static <TMsg> void connect(Producer<TMsg> producer, Consumer<TMsg> consumer) {
		producer.addConsumer(consumer);
	}
}
