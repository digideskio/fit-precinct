package de.konqi.fitapi.rest.webapi;

import de.konqi.fitapi.rest.webapi.filter.OAuthRequestFilter;
import de.konqi.fitapi.rest.webapi.filter.XOriginFilter;
import de.konqi.fitapi.rest.webapi.resource.BlobResource;
import de.konqi.fitapi.rest.webapi.resource.UserResource;
import de.konqi.fitapi.rest.webapi.resource.WorkoutResource;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.media.multipart.MultiPartProperties;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.server.mvc.jsp.JspMvcFeature;

import javax.ws.rs.ApplicationPath;
import java.util.logging.Logger;

/**
 * Web application api
 *
 * @author konqi
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

        MultiPartProperties multiPartProperties = new MultiPartProperties();
        multiPartProperties.bufferThreshold(-1);
        register(multiPartProperties);

        register(OAuthRequestFilter.class);
        register(XOriginFilter.class);
        register(UserResource.class);
        register(WorkoutResource.class);
        register(BlobResource.class);

        // Register an instance of LoggingFilter.
        register(new LoggingFilter(LOGGER, true));

        // Enable Tracing support.
        // property(ServerProperties.TRACING, "ALL");
    }
}
