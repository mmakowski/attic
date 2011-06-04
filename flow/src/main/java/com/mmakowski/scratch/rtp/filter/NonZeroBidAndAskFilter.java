package com.mmakowski.scratch.rtp.filter;

import static com.mmakowski.scratch.rtp.reuters.ReutersFields.ASK;
import static com.mmakowski.scratch.rtp.reuters.ReutersFields.BID;

import com.mmakowski.mekong.Filter;
import com.mmakowski.scratch.rtp.reuters.ReutersMessage;

public class NonZeroBidAndAskFilter extends Filter<ReutersMessage> {
	@Override
	protected boolean accept(ReutersMessage message) {
		return !isZero(message.get(BID)) && !isZero(message.get(ASK));
	}

	private boolean isZero(String numStr) {
		return numStr != null && Double.valueOf(numStr) == 0.0;
	}
}
