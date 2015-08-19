package de.konqi.fitapi.rest.webapi.resource;

import de.konqi.fitapi.rest.openfitapi.resources.Credential;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

/**
 * Created by konqi on 19.08.2015.
 */
@Path("/user")
public class User {
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/me")
    public Response login(Credential credential, @Context HttpServletResponse response, @Context SecurityContext sc){
        sc.
        return Response.ok().entity().build();
    }
}
