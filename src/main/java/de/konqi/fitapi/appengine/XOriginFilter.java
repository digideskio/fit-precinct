package de.konqi.fitapi.appengine;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by konqi on 18.09.2015.
 */
public class XOriginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

            String origin = httpServletRequest.getHeader("Origin");
            if (origin == null)
                origin = httpServletRequest.getProtocol() + "://"
                        + httpServletRequest.getServerName()
                        + (httpServletRequest.getServerPort() < 0 ? ":" + httpServletRequest.getServerPort() : "");

            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin); // "http://localhost:9000, http://localhost:8080"
            httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Session");
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
