package de.konqi.fitapi.rest.webapi.resource;

import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
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
     *
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
     *
     * @param number number of elements to return
     * @param sc     security context injected via jersey
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
     *
     * @param since workouts must be newer than this timestamp
     * @param sc    security context injected via jersey
     * @return list of workouts
     */
    @GET
    @Path("/list/since/{since}")
    public Response getWorkoutListSince(@PathParam("since") Long since, @Context SecurityContext sc) {
        return getWorkoutListSince(since, null, sc);
    }

    /**
     * Gets a list of workouts in a timespan between since and until
     *
     * @param since workouts must be newer than this timestamp
     * @param until workouts must be older than this timestamp
     * @param sc    security context injected via jersey
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
     *
     * @param workoutId id of the workout
     * @param sc        security context injected via jersey
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
     * Gets a specific workout head by its id
     *
     * @param workoutId id of the workout
     * @param sc        security context injected via jersey
     * @return workout data
     */
    @GET
    @Path("/get/{workoutId}/head")
    public Response getWorkoutHead(@PathParam("workoutId") Long workoutId, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        Workout workoutHead = WorkoutRepository.getWorkoutHeadForUser(webApiUser, workoutId);

        return Response.ok().entity(workoutHead).build();
    }

    /**
     * Updates a specific workout identified by its id
     *
     * @param workoutId id of the workout to update
     * @param workout   data to update the workout with
     * @param sc        security context injected via jersey
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
     *
     * @param workoutId id of the workout to delete
     * @param sc        security context injected via jersey
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
     * Creates a new workout head without any data attached
     *
     * @param workout workout data to insert
     * @param sc      security context injected via jersey
     * @return workout as inserted into the database
     */
    @POST
    @Path("/createHead")
    public Response createWorkoutHead(Workout workout, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        workout = WorkoutRepository.insertHead(webApiUser, workout);
        if (workout.getId() != null) {
            return Response.accepted().entity(workout).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    /**
     * Appends sensor data (or any other kind of data) to a workout identified by its id
     *
     * @param workoutId workout to append data to
     * @param data      data to append to workout
     * @param sc        security context injected via jersey
     * @return Response with Status.ACCEPTED if data was successfully appended
     */
    @POST
    @Path("/append/{workoutId}")
    public Response appendData(@PathParam("workoutId") Long workoutId, WorkoutData data, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        if (WorkoutRepository.appendData(webApiUser, workoutId, data)) {
            return Response.accepted().build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
