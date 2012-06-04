package com.mmakowski.osgi.platform;

import com.mmakowski.osgi.platform.api.Document;
import com.mmakowski.osgi.platform.api.DocumentBuilder;

class SimpleDocumentBuilder implements DocumentBuilder {
    private final String id;
    private String content;
    
    public SimpleDocumentBuilder(Document basedOn) {
        id = basedOn.getId();
        content = basedOn.getContent(); 
    }

    public DocumentBuilder withContent(String content) {
        this.content = content; 
        return this;
    }

    public Document build() {
        return new SimpleDocument(id, content);
    }

}
