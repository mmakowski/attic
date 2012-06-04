package com.mmakowski.osgi.platform.api;

public interface DocumentBuilder {
    DocumentBuilder withContent(String content);
    Document build();
}
