package com.coldana.coldana.resources;

import com.coldana.coldana.config.DatabaseConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.sql.Connection;

@Path("/database")
public class DatabaseResource {

    @GET
    @Path("/check")
    @Produces(MediaType.APPLICATION_JSON)
    public String checkDatabaseConnection() {
        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn != null) {
                return "{ \"status\": \"success\", \"message\": \"Database connected successfully!\" }";
            } else {
                return "{ \"status\": \"error\", \"message\": \"Database connection failed!\" }";
            }
        } catch (Exception e) {
            return "{ \"status\": \"error\", \"message\": \"" + e.getMessage() + "\" }";
        }
    }
}