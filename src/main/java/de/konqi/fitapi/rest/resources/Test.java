package de.konqi.fitapi.rest.resources;

import de.konqi.fitapi.db.Workout;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/test")
public class Test {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTest() {
        Workout entity = new Workout();
        return Response.ok().build();
    }
}
