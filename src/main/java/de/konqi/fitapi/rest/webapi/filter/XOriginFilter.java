package de.konqi.fitapi.rest.webapi.filter;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import java.io.IOException;
import java.net.URL;

/**
 * Created by konqi on 21.08.2015.
 */
@Priority(Priorities.HEADER_DECORATOR)
public class XOriginFilter implements ContainerResponseFilter {
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        String origin = requestContext.getHeaders().getFirst("Origin");
        if (origin == null) {
            URL url = requestContext.getUriInfo().getBaseUri().toURL();
            origin = url.getProtocol() + "://"
                    + url.getHost()
                    + (url.getDefaultPort() < 0 ? ":" + url.getPort() : "");
        }

        responseContext.getHeaders().add("Access-Control-Allow-Origin", origin); // "http://localhost:9000, http://localhost:8080"
        responseContext.getHeaders().add("Access-Control-Allow-Credentials", true);
        responseContext.getHeaders().add("Access-Control-Allow-Headers", "Content-Type, Session");
        responseContext.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
    }
}
