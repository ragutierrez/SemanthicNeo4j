package com.mycompany.services;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

// Set the path, version 1 of API
@Path("/item")
public class ItemResourceImpl {

    @GET
    @Produces("application/json")
    public List<Item> getItems() {
        List<Item> items = new ArrayList<Item>();
        items.add(new Item(100, "Widget", "A basic widget"));
        items.add(new Item(200, "SuperWidget", "A super widget"));
        items.add(new Item(300, "UberSuperWidget", "A uber super widget"));

        return items;
    }

    @GET
    @Path("/add/{a}/{b}")
    @Produces("application/json")
    public String addPlainText(@PathParam("a") int a, @PathParam("b") int b) {
        return "{\"suma\":{\"valores\":["+a+","+b+"],\"resultado\":"+(a + b) + "}}";
    }
}