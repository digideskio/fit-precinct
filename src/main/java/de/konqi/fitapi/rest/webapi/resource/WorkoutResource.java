package de.konqi.fitapi.rest.webapi.resource;

import de.konqi.fitapi.db.domain.Workout;
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
 * API resource for workout related information
 *
 * @author konqi
 */
@Path("/workout")
@RolesAllowed("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class WorkoutResource {
    /**
     * Gets a list of workouts
     * @param sc security context injected via jersey
     * @return list of workouts
     */
    @GET
    @Path("/list")
    public Response getWorkoutList(@Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getWorkoutListForUser(webApiUser);

        return Response.ok().entity(workoutListForUser).build();
    }

    /**
     * Gets a list with the #number latest workouts
     * @param number number of elements to return
     * @param sc security context injected via jersey
     * @return list of workouts
     */
    @GET
    @Path("/list/{number}")
    public Response getWorkoutList(@PathParam("number") Integer number, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getLastWorkoutsForUser(webApiUser, number);

        return Response.ok().entity(workoutListForUser).build();
    }

    /**
     * Gets a list of workouts newer than a certain time
     * @param since timestamp workouts should be newer than
     * @param sc security context injected via jersey
     * @return list of workouts
     */
    @GET
    @Path("/list/since/{since}")
    public Response getWorkoutListSince(@PathParam("since") Long since, @Context SecurityContext sc) {
        return getWorkoutListSince(since, null, sc);
    }

    /**
     * Gets a list of workouts in a timespan between since and until
     * @param since timestamp to include workouts from
     * @param until timestamp to include workouts to
     * @param sc security context injected via jersey
     * @return list of workouts
     */
    @GET
    @Path("/list/since/{since}/until/{until}")
    public Response getWorkoutListSince(@PathParam("since") Long since, @PathParam("until") Long until, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        Date sinceDate = since != null ? new Date(since) : null;
        Date untilDate = until != null ? new Date(until) : null;
        List<de.konqi.fitapi.db.domain.Workout> workoutListForUser = WorkoutRepository.getWorkoutListForUser(webApiUser, sinceDate, untilDate);

        return Response.ok().entity(workoutListForUser).build();
    }

    /**
     * Gets a specific workout by its id
     * @param workoutId id of the workout
     * @param sc security context injected via jersey
     * @return workout data
     */
    @GET
    @Path("/get/{workoutId}")
    public Response getWorkout(@PathParam("workoutId") Long workoutId, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<Object> workoutForUser = WorkoutRepository.getWorkoutForUser(webApiUser, workoutId);

        return Response.ok().entity(workoutForUser).build();
    }

    /**
     * Updates a specific workout identified by its id
     * @param workoutId id of the workout to update
     * @param workout data to update the workout with
     * @param sc security context injected via jersey
     * @return updated workout data
     */
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

    /**
     * Deletes a specific workout identified by its id
     * @param workoutId id of the workout to delete
     * @param sc security context injected via jersey
     * @return status code < 300 if successful
     */
    @DELETE
    @Path("/delete/{workoutId}")
    public Response deleteWorkout(@PathParam("workoutId") Long workoutId, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        if (WorkoutRepository.deleteWorkoutForUser(webApiUser, workoutId)) {
            return Response.accepted().build();
        }

        return Response.status(HttpStatus.SC_FORBIDDEN).build();
    }

    /**
     * Inserts a new workout
     * @param workout workout data to insert
     * @param sc security context injected via jersey
     * @return workout as inserted into the database
     */
    @POST
    @Path("/insert")
    public Response insertWorkout(Workout workout, @Context SecurityContext sc) {
        return Response.notModified().build();
    }

}
