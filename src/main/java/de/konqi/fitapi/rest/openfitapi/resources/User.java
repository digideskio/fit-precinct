package de.konqi.fitapi.rest.openfitapi.resources;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/user")
public class User {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(Credential credential, @Context HttpServletResponse response){
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setSessionName("blafoo");
        loginResponse.setSessionId("foobla");

        NewCookie newCookie = new NewCookie(loginResponse.getSessionName(), loginResponse.getSessionId());
        return Response.ok().entity(loginResponse).cookie(newCookie).build();
    } 
    
}
