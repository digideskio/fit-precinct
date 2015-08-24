package de.konqi.fitapi.rest.webapi.filter;

import de.konqi.fitapi.common.Principal;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.repository.SessionRepository;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Cookie;
import java.io.IOException;

/**
 * Created by konqi on 16.08.2015.
 */
@Priority(Priorities.AUTHENTICATION)
public class OAuthRequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(OAuthRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        String sessionId = containerRequestContext.getHeaderString("Session");
        // String authorization = containerRequestContext.getHeaderString("Authorization");
        if (sessionId != null)
        {
            User user = SessionRepository.getSession(sessionId);
            if(user != null) {

//            GoogleIdTokenVerifier googleTokenChecker = new GoogleIdTokenVerifier("", Collections.singletonList(Constants.CLIENT_ID));
//            GenericResponse<GoogleIdToken.Payload> payload = googleTokenChecker.check(authorization);
//            if (payload.isSuccess()) {
//                payload.getValue();
//
//                // TODO load principal for payload
//
//                // Create security context for principal
                WebApiUser webApiUser = new WebApiUser(user);
//                // TODO Set principal in context
//                // webApiUser.
//
//                // Set security context into request
                containerRequestContext.setSecurityContext(webApiUser);
//            }

                // TODO Defer to login
            }
        }
    }

//    private void getLoginUser(){
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
