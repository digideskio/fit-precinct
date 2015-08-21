package de.konqi.fitapi.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by konqi on 21.08.2015.
 */
public class TokenResponse {
    /**
     * A token that can be sent to a Google API.
     */
    @JsonProperty("access_token")
    private String accessToken;
    /**
     * A JWT that contains identity information about the user that is digitally signed by Google.
     */
    @JsonProperty("id_token")
    private String idToken;

    /**
     * The remaining lifetime of the access token.
     */
    @JsonProperty("expires_in")
    private Long expiresIn;

    /**
     * Identifies the type of token returned. At this time, this field always has the value Bearer.
     */
    @JsonProperty("token_type")
    private String tokenType;

    /**
     * (optional) This field is only present if access_type=offline is included in the authentication request. For details, see Refresh tokens.
     */
    @JsonProperty("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
