package de.konqi.fitapi.rest.openfitapi;

import de.konqi.fitapi.db.domain.User;

import javax.ws.rs.core.SecurityContext;
import java.security.Principal;

/**
 * Created by konqi on 17.08.2015.
 */
public class OpenFitApiUser extends de.konqi.fitapi.common.Principal implements SecurityContext {
    public OpenFitApiUser() {
        super();
    }

    public OpenFitApiUser(de.konqi.fitapi.common.Principal principal) {
        super(principal);
    }

    public OpenFitApiUser(de.konqi.fitapi.common.User user) {
        super(user);
    }

    public OpenFitApiUser(User user) {
        super(user);
    }

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

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String getAuthenticationScheme() {
        return null;
    }
}
