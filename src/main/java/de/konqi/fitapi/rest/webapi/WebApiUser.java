package de.konqi.fitapi.rest.webapi;

import de.konqi.fitapi.db.domain.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by konqi on 19.08.2015.
 */
public class WebApiUser extends de.konqi.fitapi.common.Principal implements SecurityContext {
    public WebApiUser(){
        super();
    }

    public WebApiUser(de.konqi.fitapi.common.Principal principal){
        super(principal);
    }

    public WebApiUser(de.konqi.fitapi.common.User user){
        super(user);
    }

    public WebApiUser(User user){
        super(user);
    }

    @Override
    public Principal getUserPrincipal() {
        return this;
    }

    @Override
    public boolean isUserInRole(String role) {
        de.konqi.fitapi.common.Principal userPrincipal = (de.konqi.fitapi.common.Principal) getUserPrincipal();
        return userPrincipal != null && (role.equals("user") || userPrincipal.getRoles().contains(role));
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
