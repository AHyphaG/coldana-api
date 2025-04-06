package com.coldana.coldana.resources;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.coldana.coldana.models.Users;
import com.coldana.coldana.repositories.UsersRepository;
import com.coldana.coldana.services.AuthService;
import com.coldana.coldana.utils.TokenUtil;

@Path("/auth")
public class AuthResource {
    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Map<String, String> credential){
        String username = credential.get("username");
        String password = credential.get("password");

        UsersRepository usersRepository = new UsersRepository();
        AuthService authService = new AuthService(usersRepository);

        try {
            Optional<Users> user = usersRepository.findByUsername(username);
            if (authService.login(username, password) && user.isPresent()) {

                // Generate token using the user ID
                String token = TokenUtil.generateToken(user.get().getId());

                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful!");
                response.put("token", token);

                return Response.ok(response).build();
            } else {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity(Collections.singletonMap("message", "Invalid credentials"))
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Server error").build();
        }
    }
}