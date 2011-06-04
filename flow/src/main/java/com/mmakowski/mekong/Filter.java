package com.mmakowski.mekong;

/**
 * Filters out messages based on some criteria. Subclasses of this class define
 * accept() method that specify the filtering criteria.
 *
 * @param <TMsg> the type of messages which are filtered by this Filter
 */
public abstract class Filter<TMsg> implements Processor<TMsg> {
	private final ProducerDelegate<TMsg> delegate = new ProducerDelegate<TMsg>();
	
	@Override
	public void addConsumer(Consumer<TMsg> consumer) {
		delegate.addConsumer(consumer);
	}

	@Override
	public void consume(TMsg message) {
		if (accept(message)) delegate.produce(message);
	}

	/**
	 * @return true if the message should be produced (i.e. forwarded for further processing), 
	 *         false if it should be filtered out.
	 */
	protected abstract boolean accept(TMsg message);
}
