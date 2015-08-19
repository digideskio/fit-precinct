package de.konqi.fitapi.rest.webapi;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by konqi on 19.08.2015.
 */
public class WebApiUser implements SecurityContext {
    @Override
    public Principal getUserPrincipal() {
        return null;
    }

    @Override
    public boolean isUserInRole(String role) {
        return false;
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
