package com.mmakowski.scratch.rtp.reuters;

import java.util.HashMap;
import java.util.Map;

public class ReutersMessage {
	private final Map<String, String> fields = new HashMap<String, String>();
	
	public static ReutersMessage of(String... keysAndValues) {
		ReutersMessage msg = new ReutersMessage();
		for (int i = 0; i < keysAndValues.length / 2; i++) {
			msg.put(keysAndValues[2 * i], keysAndValues[2 * i + 1]);
		}
		return msg;
	}

	private void put(String key, String value) {
		fields.put(key, value);
	}
	
	public String get(String key) {
		return fields.get(key);
	}
}
