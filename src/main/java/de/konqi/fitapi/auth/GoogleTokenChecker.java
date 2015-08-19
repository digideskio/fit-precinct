package de.konqi.fitapi.auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import de.konqi.fitapi.Utils;
import de.konqi.fitapi.common.GenericResponse;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public class GoogleTokenChecker {
    private static final GoogleIdTokenVerifier TOKEN_VERIFIER = new GoogleIdTokenVerifier(Utils.urlFetchTransport, Utils.jsonFactory);

    private List clientIds;
    private String audience = "";

    public GoogleTokenChecker(String audience, List clientIds) {
        this.audience = audience;
        this.clientIds = clientIds;
    }

    public GenericResponse<GoogleIdToken.Payload> check(String tokenString) {
        try {
            GoogleIdToken token = GoogleIdToken.parse(Utils.jsonFactory, tokenString);
            if (TOKEN_VERIFIER.verify(token)) {
                GoogleIdToken.Payload payload = token.getPayload();
                if (!payload.getAudience().equals(audience))
                    return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Audience mismatch").build();
                else if (!clientIds.contains(payload.getAuthorizedParty()))
                    return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Client ID mismatch").build();
                else
                    return new GenericResponse.Builder<GoogleIdToken.Payload>(true).withValue(payload).build();
            }
        } catch (GeneralSecurityException e) {
            return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Security issue: " + e.getLocalizedMessage()).build();
        } catch (IOException e) {
            return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Security issue: " + "Network problem: " + e.getLocalizedMessage()).build();
        }

        return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Verification failed. (Time-out?)").build();
    }
}