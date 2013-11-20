/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.services;

import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 *
 * @author ragutierrez
 */
@ApplicationPath("")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.mycompany.services.ItemResource.class);
        resources.add(com.mycompany.services.ItemResourceImpl.class);
        resources.add(com.mycompany.services.ServiceResource.class);
    }
}
