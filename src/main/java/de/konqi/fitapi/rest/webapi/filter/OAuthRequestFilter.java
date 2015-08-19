package de.konqi.fitapi.rest.webapi.filter;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import de.konqi.fitapi.Constants;
import de.konqi.fitapi.auth.GoogleTokenChecker;
import de.konqi.fitapi.common.GenericResponse;
import de.konqi.fitapi.rest.openfitapi.OpenFitApiUser;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Scanner;

/**
 * Created by konqi on 16.08.2015.
 */
@Priority(Priorities.AUTHENTICATION)
public class OAuthRequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(OAuthRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String authorization = containerRequestContext.getHeaderString("Authorization");
        GoogleTokenChecker googleTokenChecker = new GoogleTokenChecker("", Constants.CLIENT_IDS);
        GenericResponse<GoogleIdToken.Payload> payload = googleTokenChecker.check(authorization);
        if(payload.isSuccess()){
            payload.getValue();

            // TODO load principal for payload

            // Create security context for principal
            WebApiUser webApiUser = new WebApiUser();
            // TODO Set principal in context
            // webApiUser.

            // Set security context into request
            containerRequestContext.setSecurityContext(webApiUser);
        }

        // TODO Defer to login
    }

//    private void foo(){
//    // Create a state token to prevent request forgery.
//    // Store it in the session for later validation.
//    String state = new BigInteger(130, new SecureRandom()).toString(32);
//    request.session().attribute("state", state);
//    // Read index.html into memory, and set the client ID,
//    // token state, and application name in the HTML before serving it.
//    return new Scanner(new File("index.html"), "UTF-8")
//            .useDelimiter("\\A").next()
//    .replaceAll("[{]{2}\\s*CLIENT_ID\\s*[}]{2}", Constants.CLIENT_ID)
//    .replaceAll("[{]{2}\\s*STATE\\s*[}]{2}", state)
//    .replaceAll("[{]{2}\\s*APPLICATION_NAME\\s*[}]{2}",
//            Constants.APPLICATION_NAME);
//    }
}
