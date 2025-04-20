package com.coldana.coldana.resources;

import com.coldana.coldana.middleware.AuthRequired;
import com.coldana.coldana.models.Category;
import com.coldana.coldana.services.CategoryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/categories")
public class CategoryResource {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response getCategories(
            @QueryParam("isDaily") Boolean isDaily,
            @Context ContainerRequestContext requestContext
    ) {
        try {
            String userId = (String) requestContext.getProperty("userId");

            CategoryService categoryService = new CategoryService();

            List<Category> categories;
            if (isDaily != null) {
                categories = categoryService.getCategoriesByUserAndType(userId, isDaily);
            } else {
                categories = categoryService.getAllCategoriesByUser(userId);
            }

            return Response.ok(categories).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}