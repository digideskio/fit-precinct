package de.konqi.fitapi.rest.openfitapi.filter;

import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.repository.SessionRepository;
import de.konqi.fitapi.rest.openfitapi.OpenFitApi;
import de.konqi.fitapi.rest.openfitapi.OpenFitApiUser;
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
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Cookie cookie = containerRequestContext.getCookies().get(OpenFitApi.SESSION_HEADER_NAME);
        if (cookie != null) {
            String userSession = cookie.getValue();
            if (userSession != null) {
                User user = SessionRepository.getSession(userSession);
                if (user != null) {
                    logger.info("Session authenticated as user '" + user.getId() + "'.");
                    OpenFitApiUser openFitApiUser = new OpenFitApiUser(user);
                    containerRequestContext.setSecurityContext(openFitApiUser);
                } else {
                    logger.info("Invalid session.");
                }
            } else {
                logger.debug("No session");
            }
        }
    }
}
