package de.konqi.fitapi.rest.webapi;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by konqi on 19.08.2015.
 */
public class WebApiUser extends de.konqi.fitapi.common.Principal implements SecurityContext {
    @Override
    public Principal getUserPrincipal() {
        return this;
    }

    @Override
    public boolean isUserInRole(String role) {
        de.konqi.fitapi.common.Principal userPrincipal = (de.konqi.fitapi.common.Principal) getUserPrincipal();
        return userPrincipal.getRoles().contains(role);
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
