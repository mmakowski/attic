package com.mmakowski.scratch.rtp.fxspot;

import java.util.Map;

public class FXSpotDocumentBuilder extends AbstractDocumentBuilder {
	@Override
	protected String buildPath() {
		return "FXSpot/live/LONDON/INTRADAY/reuters";
	}

	@Override
	protected String buildContent() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, BidAsk> entry : rates.entrySet()) {
			BidAsk bidAsk = entry.getValue();
			sb.append(entry.getKey())
			  .append(": ").append(bidAsk.bid.toPlainString())
			  .append("/").append(bidAsk.ask.toPlainString())
			  .append("\n");
		}
		return sb.toString();
	}
}
