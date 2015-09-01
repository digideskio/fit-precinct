package de.konqi.fitapi.rest.webapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @Override
    public Principal getUserPrincipal() {
        return this;
    }

    @Override
    public boolean isUserInRole(String role) {
        // FIXME Need more complex logic to determine role membership
        de.konqi.fitapi.common.Principal userPrincipal = (de.konqi.fitapi.common.Principal) getUserPrincipal();
        return userPrincipal != null && (role.equals("user") || userPrincipal.getRoles().contains(role));
    }

    @JsonIgnore
    @Override
    public boolean isSecure() {
        return false;
    }

    @JsonIgnore
    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
