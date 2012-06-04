package com.mmakowski.osgi.platform;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import com.mmakowski.osgi.platform.api.app.Application;
import com.mmakowski.osgi.platform.api.services.DocumentPersistenceService;

public class Activator implements BundleActivator, ServiceListener {
    private BundleContext context;
    
    
    public void serviceChanged(ServiceEvent event) {
        final ServiceReference serviceRef = event.getServiceReference();
        switch (event.getType()) {
        case ServiceEvent.REGISTERED: 
            final String[] objectClass = (String[]) serviceRef.getProperty("objectClass");
            if (objectClass[0].equals(Application.class.getName())) runApplication(serviceRef);
            break;
        default:
            System.out.println("Unsupported event type " + event.getType() + " for service " + serviceRef);
        }
    }

    private void runApplication(ServiceReference applicationRef) {
        try {
            final String persistenceSvcName = DocumentPersistenceService.class.getName();
            ServiceReference persistenceRef = context.getServiceReference(persistenceSvcName);
            if (persistenceRef == null) throw new RuntimeException("no services found for " + persistenceSvcName);
            final DocumentPersistenceService persistence = (DocumentPersistenceService) context.getService(persistenceRef);
            final Application app = (Application) context.getService(applicationRef);
            app.execute(persistence);
        } catch (Exception e) {
            System.err.println("internal error when running application" + applicationRef + ": " + e.getMessage());
        }        
    }

    public void start(BundleContext context) throws Exception {
        this.context = context;
        context.addServiceListener(this);
        context.registerService(DocumentPersistenceService.class.getName(), new SimpleDocumentPersistence(), new Properties());
        // TODO: find all applications that have been registered already
        System.out.println("started platform");
    }

    public void stop(BundleContext context) throws Exception {
        context.removeServiceListener(this);
        System.out.println("stopped platform");
    }
}
