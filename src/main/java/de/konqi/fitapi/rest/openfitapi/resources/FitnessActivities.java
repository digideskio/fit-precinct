package de.konqi.fitapi.rest.openfitapi.resources;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/")
public class FitnessActivities {
    private static final Logger logger = LoggerFactory.getLogger(FitnessActivities.class);

    @GET
    @Path("/fitnessActivities/{itemId}{a:|.json}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkout(@PathParam("itemId") Long itemId) {
        logger.info("GET " + itemId);

        ObjectNode rootNode = ResourceUtils.loadWorkout(itemId);

        return Response.ok().entity(rootNode).build();
    }

    @POST
    @Path("/fitnessActivities{a:|.json}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWorkout(Workout workout, @Context UriInfo uriInfo) {
        Long workoutId = ResourceUtils.storeWorkout(workout);

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/fitnessActivities/{itemId}.json");
        URI uri = uriBuilder.build(workoutId);
        logger.info("uri: " + uri.toString());

        return Response.ok().link(uri, "self").build();
        // returns String ??
    }


    @GET
    @Path("/fitnessActivities{a:|.json}")
    public Response getWorkoutList() {
        WorkoutList workoutList = new WorkoutList();

        return Response.ok().entity(workoutList).build();
    }

    @PUT
    @Path("/fitnessActivities/{itemId}{a:|.json}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWorkout(@PathParam("itemId") String itemId, Workout workout) {
        // returns String ??
        return Response.ok().build();
    }
}
