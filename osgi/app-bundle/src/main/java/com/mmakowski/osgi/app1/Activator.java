package com.mmakowski.osgi.app1;

import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.mmakowski.osgi.platform.api.app.Application;

public class Activator implements BundleActivator {
    public void start(BundleContext context) throws Exception {
        context.registerService(Application.class.getName(), new App(), new Properties());        
    }

    public void stop(BundleContext context) throws Exception {
        // no need to do anything, services will be unregistered by the framework
    }
}
