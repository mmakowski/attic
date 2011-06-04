package com.mmakowski.scratch.xds;

public class XdsDocument {
	private final String path;
	private final String content;
	
	public XdsDocument(String path, String content) {
		this.path = path;
		this.content = content;
	}
	
	public String getPath() {
		return path;
	}
	
	public String getContent() {
		return content;
	}
}
