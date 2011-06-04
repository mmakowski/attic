package com.mmakowski.scratch.rtp.fxspot;

import java.util.Map;

public class FXSpotRatesDocumentBuilder extends AbstractDocumentBuilder {
	@Override
	protected String buildPath() {
		return "FXSpotRates/live/LONDON/INTRADAY";
	}

	@Override
	protected String buildContent() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, BidAsk> entry : rates.entrySet()) {
			BidAsk bidAsk = entry.getValue();
			sb.append(entry.getKey())
			  .append(": bid=").append(bidAsk.bid.toPlainString())
			  .append(", ask=").append(bidAsk.ask.toPlainString())
			  .append("\n");
		}
		return sb.toString();
	}
}
