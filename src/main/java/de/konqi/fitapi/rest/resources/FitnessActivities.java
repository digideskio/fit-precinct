package de.konqi.fitapi.rest.resources;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/")
public class FitnessActivities {
    private static final Logger logger = LoggerFactory.getLogger(FitnessActivities.class);
    
    @GET
    @Path("/fitnessActivities/{itemId}.json")
    // @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkout(@PathParam("itemId") String itemId){
        logger.info("GET " + itemId);
        
        return Response.ok().build();
    }

    @POST
    @Path("/fitnessActivities{a:|.json}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addWorkout(Workout workout){
        // returns String ??
        if( workout.getLocation() != null){
            for(int i=0;i<workout.getLocation().length;i+=2){
                JsonNode offset = workout.getLocation()[i];
                JsonNode value = workout.getLocation()[i + 1];
                logger.info(offset.asInt() + " => " + value.get(0).asText() + ", " + value.get(1).asText());
            }
        }
        
        return Response.ok().build();

    }

    @GET
    @Path("/fitnessActivities{a:|.json}")
    public Response getWorkoutList(){
        WorkoutList workoutList = new WorkoutList();

        return Response.ok().entity(workoutList).build();
    }

    @PUT
    @Path("/fitnessActivities/{itemId}.json")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateWorkout(@PathParam("itemId") String itemId, Workout workout){
        // returns String ??
        return Response.ok().build();
    }
}
