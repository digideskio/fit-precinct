package de.konqi.fitapi.rest.webapi.resource;

import de.konqi.fitapi.db.repository.WorkoutRepository;
import de.konqi.fitapi.rest.webapi.WebApiUser;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
@Path("/workout")
@RolesAllowed("user")
public class Workout {
    @GET
    @Path("/list")
    public Response getWorkoutList(@Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        List<Workout> workoutListForUser = WorkoutRepository.getWorkoutListForUser(webApiUser);

        return Response.ok().entity(workoutListForUser).build();
    }
}
