package com.mmakowski.scratch;

import static com.mmakowski.scratch.rtp.reuters.ReutersFields.ASK;
import static com.mmakowski.scratch.rtp.reuters.ReutersFields.BID;
import static com.mmakowski.scratch.rtp.reuters.ReutersFields.RIC;
import static org.junit.Assert.*;

import org.junit.Test;

import com.mmakowski.mekong.Consumer;
import com.mmakowski.mekong.ProducerDelegate;
import com.mmakowski.scratch.rtp.reuters.ReutersMessage;
import com.mmakowski.scratch.xds.XdsDocument;

public class FlowAppIntTest {
	@Test
	public void updatesAreReceived() throws InterruptedException {
		verifyAfterTwoMessages("2133.31", "2233.45", "2133.32", "2233.46", "2133.32", "2233.46");
	}

	@Test
	public void zeroRatesAreIgnored() throws InterruptedException {
		verifyAfterTwoMessages("2133.31", "2233.45", "0.0", "2233.45", "2133.31", "2233.45");
	}

	private void verifyAfterTwoMessages(final String bid1, final String ask1,
			final String bid2, final String ask2, final String expectedBid,
			final String expectedAsk) throws InterruptedException {
		TestSource source = new TestSource() {
			@Override
			public void run() {
				produce(ReutersMessage.of(RIC, "JPY=", BID, bid1, ASK, ask1));
				produce(ReutersMessage.of(RIC, "JPY=", BID, bid2, ASK, ask2));
			}
		};
		TestDestination destination = new TestDestination() {
			@Override
			public void verify() {
				assertEquals("FXSpot", "JPY=: " + expectedBid + "/" + expectedAsk + "\n",
						fxSpotDoc.getContent());
				assertEquals("FXSpotRates", "JPY=: bid=" + expectedBid + ", ask=" + expectedAsk + "\n",
						fxSpotRatesDoc.getContent());
			}
		};
		FlowApp.assemble(source, destination);
		source.run();
		Thread.sleep(1000);
		destination.verify();
	}

	private static abstract class TestSource extends ProducerDelegate<ReutersMessage> {
		public abstract void run();
	}

	private static abstract class TestDestination implements Consumer<XdsDocument> {
		protected XdsDocument fxSpotDoc;
		protected XdsDocument fxSpotRatesDoc;

		@Override
		public void consume(XdsDocument message) {
			if (message.getPath().startsWith("FXSpotRates/")) {
				fxSpotRatesDoc = message;
			} else if (message.getPath().startsWith("FXSpot/")) {
				fxSpotDoc = message;
			} else {
				fail("unexpected document: " + message.getPath());
			}
		}

		public abstract void verify();
	}
}
