package com.mmakowski.osgi.platform.api.services;

import com.mmakowski.osgi.platform.api.Document;

public interface DocumentPersistenceService {
    Document loadDocument(String id);
    void saveDocument(Document document);
}
