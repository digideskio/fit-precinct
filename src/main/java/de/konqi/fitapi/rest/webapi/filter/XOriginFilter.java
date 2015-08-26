package de.konqi.fitapi.rest.webapi.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;

/**
 * Created by konqi on 21.08.2015.
 */
@Priority(Priorities.HEADER_DECORATOR)
public class XOriginFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaders().getFirst("Origin");
        if (origin == null)
            origin = requestContext.getUriInfo().getBaseUri().toURL().getProtocol() + "://"
                    + requestContext.getUriInfo().getBaseUri().toURL().getHost()
                    + (requestContext.getUriInfo().getBaseUri().toURL().getDefaultPort() < 0 ? ":" + requestContext.getUriInfo().getBaseUri().toURL().getPort() : "");

        responseContext.getHeaders().add("Access-Control-Allow-Origin", origin); // "http://localhost:9000, http://localhost:8080"
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", true);
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Session");
    }
}
