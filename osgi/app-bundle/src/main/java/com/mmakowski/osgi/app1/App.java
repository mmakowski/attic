package com.mmakowski.osgi.app1;

import com.mmakowski.osgi.platform.api.Document;
import com.mmakowski.osgi.platform.api.app.Application;
import com.mmakowski.osgi.platform.api.services.DocumentPersistenceService;

class App implements Application {

    public void execute(DocumentPersistenceService documentPersistence) {
        final Document doc = documentPersistence.loadDocument("100");
        documentPersistence.saveDocument(doc.withContent("new content").build());
    }

}
