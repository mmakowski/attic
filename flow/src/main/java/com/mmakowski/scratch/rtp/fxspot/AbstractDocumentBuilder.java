package com.mmakowski.scratch.rtp.fxspot;

import static com.mmakowski.scratch.rtp.reuters.ReutersFields.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.mmakowski.mekong.Consumer;
import com.mmakowski.mekong.Producer;
import com.mmakowski.mekong.ProducerDelegate;
import com.mmakowski.scratch.rtp.reuters.ReutersMessage;
import com.mmakowski.scratch.xds.XdsDocument;

abstract class AbstractDocumentBuilder implements Consumer<ReutersMessage>, Producer<XdsDocument> {
	private final ProducerDelegate<XdsDocument> delegate =  new ProducerDelegate<XdsDocument>();
	protected final Map<String, BidAsk> rates = new HashMap<String, BidAsk>(); 
	
	@Override
	public void addConsumer(Consumer<XdsDocument> consumer) {
		delegate.addConsumer(consumer);
	}

	@Override
	public void consume(ReutersMessage message) {
		rates.put(message.get(RIC), new BidAsk(message));
		delegate.produce(buildDocument());
	}
	
	private XdsDocument buildDocument() {
		return new XdsDocument(buildPath(), buildContent());
	}

	protected abstract String buildPath();
	protected abstract String buildContent();	

	protected static class BidAsk {
		public final BigDecimal bid;
		public final BigDecimal ask;
		
		public BidAsk(ReutersMessage message) {
			bid = new BigDecimal(message.get(BID));
			ask = new BigDecimal(message.get(ASK));
		}
	}
}
