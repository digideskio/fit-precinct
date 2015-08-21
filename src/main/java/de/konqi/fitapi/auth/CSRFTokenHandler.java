package de.konqi.fitapi.auth;

import de.konqi.fitapi.Utils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by konqi on 21.08.2015.
 */
public class CSRFTokenHandler {
    private static final String CSRF_TOKEN_PREFIX = "CSRF_";

    /**
     * Creates a one time CSRF token
     * @param sessionInformation
     * @return
     */
    public static String createToken(Object sessionInformation){
        if(sessionInformation == null) sessionInformation = true;
        String state = new BigInteger(130, new SecureRandom()).toString(32);
        Utils.memcacheService.put(CSRF_TOKEN_PREFIX + state, sessionInformation);

        return state;
    }

    /**
     * Get and revoke CSRF token
     * @param state
     * @return
     */
    public static Object getToken(String state){
        Object sessionInformation = Utils.memcacheService.get(CSRF_TOKEN_PREFIX + state);
        Utils.memcacheService.delete(CSRF_TOKEN_PREFIX + state);

        return sessionInformation;
    }

}
