package de.konqi.fitapi.rest;

import de.konqi.fitapi.rest.resources.FitnessActivities;
import de.konqi.fitapi.rest.resources.User;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

/**
 * Created by konqi on 16.08.2015.
 */
@ApplicationPath("/openfitapi/api")
public class OpenFitApi extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger(OpenFitApi.class.getName());

    public OpenFitApi() {
        register(FitnessActivities.class);
        register(User.class);
        
        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));

        // Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");
    }


}
