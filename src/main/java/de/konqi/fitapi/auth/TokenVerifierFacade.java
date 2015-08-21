package de.konqi.fitapi.auth;

import de.konqi.fitapi.Constants;

/**
 * Created by konqi on 21.08.2015.
 */
public class TokenVerifierFacade {
    public static IdClaim verify(String providerName, String idToken) {
        TokenVerifier verifier;
        if (providerName.equals("google"))
            verifier = new GoogleIdTokenVerifier();
        else
            return null;

        IdClaim claim = verifier.verify(idToken);
        if (claim.getAudience().equals(Constants.CLIENT_ID))
            return claim;

        return null;
    }

//    private List clientIds;
//    private String audience = "";
//
//    public GoogleIdTokenVerifier(String audience, List clientIds) {
//        this.audience = audience;
//        this.clientIds = clientIds;
//    }
//
//    public GenericResponse<GoogleIdToken.Payload> check(String tokenString) {
//        try {
//            GoogleIdToken token = GoogleIdToken.parse(Utils.jsonFactory, tokenString);
//            if (TOKEN_VERIFIER.verify(token)) {
//                GoogleIdToken.Payload payload = token.getPayload();
//                if (!payload.getAudience().equals(audience))
//                    return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Audience mismatch").build();
//                else if (!clientIds.contains(payload.getAuthorizedParty()))
//                    return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Client ID mismatch").build();
//                else
//                    return new GenericResponse.Builder<GoogleIdToken.Payload>(true).withValue(payload).build();
//            }
//        } catch (GeneralSecurityException e) {
//            return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Security issue: " + e.getLocalizedMessage()).build();
//        } catch (IOException e) {
//            return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Security issue: " + "Network problem: " + e.getLocalizedMessage()).build();
//        }
//
//        return new GenericResponse.Builder<GoogleIdToken.Payload>(false).withMessage("Verification failed. (Time-out?)").build();
//    }
}
