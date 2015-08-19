package de.konqi.fitapi.rest.webapi;

import de.konqi.fitapi.rest.webapi.filter.OAuthRequestFilter;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

/**
 * Created by konqi on 19.08.2015.
 */
@ApplicationPath("/web/api")
public class WebApi extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger(WebApi.class.getName());

    public WebApi() {
        register(OAuthRequestFilter.class);

        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");
    }
}
