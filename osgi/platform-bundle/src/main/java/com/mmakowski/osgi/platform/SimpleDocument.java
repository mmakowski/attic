package com.mmakowski.osgi.platform;

import com.mmakowski.osgi.platform.api.Document;
import com.mmakowski.osgi.platform.api.DocumentBuilder;

class SimpleDocument implements Document {
    private final String id;
    private final String content;
    
    SimpleDocument(String id, String content) {
        this.id = id;
        this.content = content;
    }

    public DocumentBuilder withContent(String content) {
        return new SimpleDocumentBuilder(this).withContent(content);
    }

    public Document build() {
        return this;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
