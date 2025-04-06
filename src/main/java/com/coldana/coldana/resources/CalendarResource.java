package com.coldana.coldana.resources;

import com.coldana.coldana.middleware.AuthRequired;
import com.coldana.coldana.models.CalendarDay;
import com.coldana.coldana.services.CalendarService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
@Path("/calendar")
public class CalendarResource {

    private final CalendarService calendarService;

    public CalendarResource() {
        this.calendarService = new CalendarService();
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
}