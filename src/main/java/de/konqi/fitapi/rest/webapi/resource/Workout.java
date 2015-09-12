package de.konqi.fitapi.rest.webapi.resource;

import de.konqi.fitapi.db.repository.WorkoutRepository;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.apache.http.HttpStatus;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.Date;
import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
@Path("/workout")
@RolesAllowed("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Workout {
    @GET
    @Path("/list")
    public Response getWorkoutList(@Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getWorkoutListForUser(webApiUser);

        return Response.ok().entity(workoutListForUser).build();
    }

    @GET
    @Path("/list/{number}")
    public Response getWorkoutList(@PathParam("number") Integer number, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getLastWorkoutsForUser(webApiUser, number);

        return Response.ok().entity(workoutListForUser).build();
    }

    @GET
    @Path("/list/since/{since}")
    public Response getWorkoutListSince(@PathParam("since") Long since, @Context SecurityContext sc) {
        return getWorkoutListSince(since, null, sc);
    }


    @GET
    @Path("/list/since/{since}/until/{until}")
    public Response getWorkoutListSince(@PathParam("since") Long since, @PathParam("until") Long until, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        Date sinceDate = since != null ? new Date(since) : null;
        Date untilDate = until != null ? new Date(until) : null;
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getWorkoutListForUser(webApiUser, sinceDate, untilDate);

        return Response.ok().entity(workoutListForUser).build();
    }


    @GET
    @Path("/get/{workoutId}")
    public Response getWorkout(@PathParam("workoutId") Long workoutId, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<Object> workoutForUser = WorkoutRepository.getWorkoutForUser(webApiUser, workoutId);

        return Response.ok().entity(workoutForUser).build();
    }

    @POST
    @Path("/update/{workoutId}")
    public Response updateWorkout(@PathParam("workoutId") Long workoutId, de.konqi.fitapi.db.domain.Workout workout, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        de.konqi.fitapi.db.domain.Workout workoutForUser = WorkoutRepository.getWorkoutHeadForUser(webApiUser, workoutId);
        if (workoutForUser != null) {
            workoutForUser.setName(workout.getName());
            workoutForUser.setType(workout.getType());
            workoutForUser.setStartTime(workout.getStartTime());
            workoutForUser.setNotes(workout.getNotes());

            if (WorkoutRepository.updateWorkoutHeadForUser(webApiUser, workoutForUser)) {
                return Response.ok().entity(workoutForUser).build();
            }
        }

        return Response.status(HttpStatus.SC_FORBIDDEN).build();
    }

}
