package com.coldana.coldana;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/api")
public class HelloApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        // ======= Providers dan Resources =======
        //Providers
        resources.add(com.coldana.coldana.middleware.AuthFilter.class);

        //Resources
        resources.add(com.coldana.coldana.HelloResource.class);
        resources.add(com.coldana.coldana.resources.DatabaseResource.class);
        resources.add(com.coldana.coldana.resources.AuthResource.class);
        resources.add(com.coldana.coldana.resources.CalendarResource.class);
        resources.add(com.coldana.coldana.resources.CategoryResource.class);
        // ======= Providers dan Resources =======
        return resources;
    }
}