package com.mmakowski.osgi.platform;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import com.mmakowski.osgi.platform.api.services.DocumentPersistenceService;

public class Activator implements BundleActivator, ServiceListener {

    public void serviceChanged(ServiceEvent event) {
        String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");
        switch (event.getType()) {
        case ServiceEvent.REGISTERED: 
            System.out.println("Ex1: Service of type " + objectClass[0] + " registered.");
            break;
        case ServiceEvent.UNREGISTERING:
            System.out.println("Ex1: Service of type " + objectClass[0] + " unregistered.");
            break;
        case ServiceEvent.MODIFIED:
            System.out.println("Ex1: Service of type " + objectClass[0] + " modified.");
            break;
        default:
            System.out.println("Unsupported event type " + event.getType() + " for service of type " + objectClass[0]);
        }
    }

    public void start(BundleContext context) throws Exception {
        context.addServiceListener(this);
        context.registerService(DocumentPersistenceService.class.getName(), new SimpleDocumentPersistence(), new Properties());        
        System.out.println("started platform");
    }

    public void stop(BundleContext context) throws Exception {
        context.removeServiceListener(this);
        System.out.println("stopped platform");
    }
}
