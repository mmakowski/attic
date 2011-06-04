package com.mmakowski.scratch;

import static com.mmakowski.mekong.Flow.connect;

import com.mmakowski.mekong.Consumer;
import com.mmakowski.mekong.Producer;
import com.mmakowski.mekong.Throttler;
import com.mmakowski.scratch.rtp.filter.NonZeroBidAndAskFilter;
import com.mmakowski.scratch.rtp.fxspot.FXSpotDocumentBuilder;
import com.mmakowski.scratch.rtp.fxspot.FXSpotRatesDocumentBuilder;
import com.mmakowski.scratch.rtp.reuters.ReutersMessage;
import com.mmakowski.scratch.rtp.reuters.ReutersSubscriber;
import com.mmakowski.scratch.xds.XdsDocument;
import com.mmakowski.scratch.xds.XdsPublisher;

public class FlowApp {
	public static void main(String[] args) throws InterruptedException {
		ReutersSubscriber subscriber = new ReutersSubscriber();
		XdsPublisher publisher = new XdsPublisher();
		assemble(subscriber, publisher);
		subscriber.run();
	}
	
	public static void assemble(Producer<ReutersMessage> source, Consumer<XdsDocument> destination) {
		NonZeroBidAndAskFilter nonZeroBidAndAskFilter = new NonZeroBidAndAskFilter();
		FXSpotRatesDocumentBuilder fxSpotRatesBuilder = new FXSpotRatesDocumentBuilder();
		Throttler<XdsDocument> fxSpotRatesThrottler = new Throttler<XdsDocument>(2);
		FXSpotDocumentBuilder fxSpotBuilder = new FXSpotDocumentBuilder();
		Throttler<XdsDocument> fxSpotThrottler = new Throttler<XdsDocument>(3);

		connect(source, nonZeroBidAndAskFilter);
		connect(nonZeroBidAndAskFilter, fxSpotRatesBuilder);
		connect(nonZeroBidAndAskFilter, fxSpotBuilder);
		connect(fxSpotRatesBuilder, fxSpotRatesThrottler);
		connect(fxSpotRatesThrottler, destination);
		connect(fxSpotBuilder, fxSpotThrottler);
		connect(fxSpotThrottler, destination);
	}
}
