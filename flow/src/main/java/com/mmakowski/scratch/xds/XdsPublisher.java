package com.mmakowski.scratch.xds;

import com.mmakowski.mekong.Consumer;

/**
 * In a real implementation this would save the document to XDS. Here it just writes it to stdout.
 */
public class XdsPublisher implements Consumer<XdsDocument> {
	@Override
	public void consume(XdsDocument doc) {
		System.out.println(doc.getPath() + ":\n\n" + doc.getContent() + "\n---------------");
	}
}
