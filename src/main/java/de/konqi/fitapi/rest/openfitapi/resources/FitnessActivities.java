package de.konqi.fitapi.rest.openfitapi.resources;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.konqi.fitapi.rest.openfitapi.OpenFitApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/")
@RolesAllowed("user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FitnessActivities {
    private static final Logger logger = LoggerFactory.getLogger(FitnessActivities.class);

    @GET
    @Path("/fitnessActivities/{itemId}{a:|.json}")
    public Response getWorkout(@PathParam("itemId") Long itemId, @Context SecurityContext sc) {
        logger.info("GET " + itemId);
        ObjectNode rootNode = ResourceUtils.loadWorkout(itemId, (OpenFitApiUser) sc.getUserPrincipal());

        return Response.ok().entity(rootNode).build();
    }

    @POST
    @Path("/fitnessActivities{a:|.json}")
    public Response addWorkout(Workout workout, @Context UriInfo uriInfo, @Context SecurityContext sc) {
        Long workoutId = ResourceUtils.storeWorkout(workout, (OpenFitApiUser) sc.getUserPrincipal());

        UriBuilder uriBuilder = uriInfo.getBaseUriBuilder().path("/fitnessActivities/{itemId}.json");
        URI uri = uriBuilder.build(workoutId);
        logger.info("uri: " + uri.toString());

        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();
        ArrayNode jsonArray = rootNode.putArray("uris");
        jsonArray.add(uri.toString());

        return Response.ok().entity(rootNode).build();
    }


    @GET
    @Path("/fitnessActivities{a:|.json}")
    public Response getWorkoutList(@Context SecurityContext sc) {
        WorkoutList workoutList = new WorkoutList();

        return Response.ok().entity(workoutList).build();
    }

    @PUT
    @Path("/fitnessActivities/{itemId}{a:|.json}")
    public Response updateWorkout(@PathParam("itemId") String itemId, Workout workout, @Context SecurityContext sc) {
        // returns String ??
        return Response.ok().build();
    }
}
