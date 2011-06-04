package com.mmakowski.mekong;

/**
 * Commonly a component will consume a message of some type, perform some processing
 * and then produce a message of the same type. This interface makes it convenient to
 * define such components without the need to specify Consumer and Producer separately.
 * 
 * @param <TMsg> the type of message consumed and produced.
 */
public interface Processor<TMsg> extends Consumer<TMsg>, Producer<TMsg> {
}
