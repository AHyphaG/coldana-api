package com.coldana.coldana.resources;

import com.coldana.coldana.middleware.AuthRequired;
import com.coldana.coldana.models.CalendarDay;
import com.coldana.coldana.models.Expense;
import com.coldana.coldana.services.CalendarService;
import com.coldana.coldana.services.ExpenseService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Path("/calendar")
public class CalendarResource {

    private final CalendarService calendarService;
    private final ExpenseService expenseService;


    public CalendarResource() {
        this.calendarService = new CalendarService();
        this.expenseService = new ExpenseService();
    }

//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getCalendar(@QueryParam("userId") String userId,
//                                @QueryParam("year") int year,
//                                @QueryParam("month") int month) {
//        try {
//            List<CalendarDay> calendarDays = calendarService.generateCalendar(userId, year, month);
//            return Response.ok(calendarDays).build();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
//                    .entity("Gagal generate kalender.").build();
//        }
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response getCalendar(
            @QueryParam("start") String startStr,
            @QueryParam("end") String endStr,
            @Context ContainerRequestContext requestContext
    ) {
        try {
            LocalDate start = LocalDate.parse(startStr);
            LocalDate end = LocalDate.parse(endStr);

            // Ambil userId dari requestContext
            String userId = (String) requestContext.getProperty("userId");

            List<Map<String, Object>> result = calendarService.generateCalendar(start, end, userId);
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/expenses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response addExpense(Map<String, Object> expenseData, @Context ContainerRequestContext requestContext) {
        try {
            // Get userId from the token (set by AuthFilter)
            String userId = (String) requestContext.getProperty("userId");

            // Extract data from request
            String categoryId = (String) expenseData.get("categoryId");
            double amount = ((Number) expenseData.get("amount")).doubleValue();
            LocalDate date = LocalDate.parse((String) expenseData.get("date"));

            // Create and save the expense
            Expense expense = new Expense(
                    null, // expenseId will be generated
                    userId,
                    categoryId,
                    (int) amount, // Convert to int for your model
                    date,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            expenseService.addExpense(expense);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("message", "Expense added successfully"))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/expenses")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @AuthRequired
    public Response updateExpense(Map<String, Object> expenseData, @Context ContainerRequestContext requestContext) {
        try {
            // Get userId from the token
            String userId = (String) requestContext.getProperty("userId");

            // Extract data from request
            String categoryId = (String) expenseData.get("categoryId");
            double amount = ((Number) expenseData.get("amount")).doubleValue();
            LocalDate date = LocalDate.parse((String) expenseData.get("date"));

            // Update or create the expense for the given date and category
            expenseService.updateExpense(userId, categoryId, (int) amount, date);

            return Response.ok(Map.of("message", "Expense updated successfully")).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

}