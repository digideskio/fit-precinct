package de.konqi.fitapi.rest.webapi;

import de.konqi.fitapi.rest.webapi.filter.OAuthRequestFilter;
import de.konqi.fitapi.rest.webapi.filter.XOriginFilter;
import de.konqi.fitapi.rest.webapi.resource.User;
import de.konqi.fitapi.rest.webapi.resource.Workout;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.mvc.MvcFeature;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

/**
 * Created by konqi on 19.08.2015.
 */
@ApplicationPath("/web/api")
public class WebApi extends ResourceConfig {
    private static final Logger LOGGER = Logger.getLogger(WebApi.class.getName());

    public WebApi() {
        register(RolesAllowedDynamicFeature.class);

//        property(MvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
//        register(MvcFeature.class);
        property(JspMvcFeature.TEMPLATE_BASE_PATH, "/WEB-INF/jsp");
        register(JspMvcFeature.class);

        register(OAuthRequestFilter.class);
        register(XOriginFilter.class);
        register(User.class);
        register(Workout.class);

        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));

        // Enable Tracing support.
        // property(ServerProperties.TRACING, "ALL");
    }
}
