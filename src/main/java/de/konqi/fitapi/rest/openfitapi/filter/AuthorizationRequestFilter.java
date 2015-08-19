package de.konqi.fitapi.rest.openfitapi.filter;

import de.konqi.fitapi.rest.openfitapi.OpenFitApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import java.io.IOException;

/**
 * Created by konqi on 16.08.2015.
 */
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationRequestFilter implements ContainerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationRequestFilter.class);

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        for (String headerName : containerRequestContext.getHeaders().keySet()) {
            for (String string : containerRequestContext.getHeaders().get(headerName)) {
                logger.info("Header: " + headerName + ": " +
                        string);
            }
        }

        for (String cookieName : containerRequestContext.getCookies().keySet()) {
            logger.info("Cookie: " + containerRequestContext.getCookies().get(cookieName).getName() + ": " +
                    containerRequestContext.getCookies().get(cookieName).getValue());
        }

        // containerRequestContext.

        // TODO load User information from header
        OpenFitApiUser openFitApiUser = new OpenFitApiUser();

        containerRequestContext.setSecurityContext(openFitApiUser);
    }
}
