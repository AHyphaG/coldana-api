package com.coldana.coldana;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/")
public class HelloResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response welcomeMessage() {
        return Response.ok("Welcome to Coldana API! 🚀").build();
    }
}