package com.mmakowski.osgi.platform.api.app;

import com.mmakowski.osgi.platform.api.services.DocumentPersistenceService;

public interface Application {
    void execute(DocumentPersistenceService documentPersistence);
}
