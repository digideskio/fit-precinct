package de.konqi.fitapi.rest.openfitapi.resources;

import de.konqi.fitapi.common.HashBuilder;
import de.konqi.fitapi.db.domain.UploadCredential;
import de.konqi.fitapi.db.repository.SessionRepository;
import de.konqi.fitapi.db.repository.UserRepository;
import de.konqi.fitapi.rest.openfitapi.OpenFitApi;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by konqi on 16.08.2015.
 */
@Path("/user")
public class User {
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(Credential credential, @Context HttpServletResponse response) {
        credential.getUsername();
        credential.getPassword();

        UploadCredential uploadUserByUsername = UserRepository.getUploadUserByUsername(credential.getUsername());
        if (uploadUserByUsername != null) {
            if (uploadUserByUsername.getPassword().equals(HashBuilder.SHA256.digest(credential.getPassword()).asHex())) {
                // Create session
                String session = SessionRepository.createSession(uploadUserByUsername.getUser());

                LoginResponse loginResponse = new LoginResponse();
                loginResponse.setSessionName(OpenFitApi.SESSION_HEADER_NAME);
                loginResponse.setSessionId(session);

                return Response.ok().entity(loginResponse).build();
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }

}
