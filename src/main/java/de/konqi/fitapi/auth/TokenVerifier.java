package de.konqi.fitapi.auth;

/**
 * Created by konqi on 21.08.2015.
 */
public interface TokenVerifier {
    IdClaim verify(String idToken);
}
