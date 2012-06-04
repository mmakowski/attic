package com.mmakowski.osgi.platform;

import com.mmakowski.osgi.platform.api.Document;
import com.mmakowski.osgi.platform.api.services.DocumentPersistenceService;

class SimpleDocumentPersistence implements DocumentPersistenceService {

    public Document loadDocument(String id) {
        return new SimpleDocument(id, "content of document " + id);
    }

    public void saveDocument(Document document) {
        System.out.println("saving document " + document.getId());
    }
}
