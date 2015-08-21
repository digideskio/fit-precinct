package de.konqi.fitapi.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GooglePublicKeysManager;
import de.konqi.fitapi.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleIdTokenVerifier implements TokenVerifier {
    private static final Logger logger = LoggerFactory.getLogger(GoogleIdTokenVerifier.class);

    private static GooglePublicKeysManager googlePublicKeysManager = new GooglePublicKeysManager.Builder(Utils.urlFetchTransport, Utils.jsonFactory).build();
    private static com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier googleIdTokenVerifier = new com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier(googlePublicKeysManager);

    @Override
    public IdClaim verify(String idToken) {
        try {
            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);
            return Utils.jacksonObjectMapper.readValue(googleIdToken.getPayload().toString(), IdClaim.class);
        } catch (IOException | GeneralSecurityException e) {
            logger.warn("Unable to verify id_token.", e);
        }

        return null;
    }
}