package de.konqi.fitapi.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by konqi on 21.08.2015.
 */
public class IdClaim {
    /**
     * (always) The Issuer Identifier for the Issuer of the response. Always accounts.google.com or https://accounts.google.com for Google ID tokens.
     */
    @JsonProperty("iss")
    private String isssuer;
    /**
     * Access token hash. Provides validation that the access token is tied to the identity token. If the ID token is issued with an access token in the server flow, this is always included. This can be used as an alternate mechanism to protect against cross-site request forgery attacks, but if you follow Step 1 and Step 3 it is not necessary to verify the access token.
     */
    @JsonProperty("at_hash")
    private String accessTokenHash;
    /**
     * True if the user's e-mail address has been verified; otherwise false.
     */
    @JsonProperty("email_verified")
    private Boolean emailVerified;
    /**
     * (always) An identifier for the user, unique among all Google accounts and never reused. A Google account can have multiple emails at different points in time, but the sub value is never changed. Use sub within your application as the unique-identifier key for the user.
     */
    @JsonProperty("sub")
    private String subscriber;
    /**
     * The client_id of the authorized presenter. This claim is only needed when the party requesting the ID token is not the same as the audience of the ID token. This may be the case at Google for hybrid apps where a web application and Android app have a different client_id but share the same project.
     */
    @JsonProperty("azp")
    private String authorizedPresenter;
    /**
     * The user’s email address. This may not be unique and is not suitable for use as a primary key. Provided only if your scope included the string "email".
     */
    @JsonProperty("email")
    private String email;
    /**
     * (always) Identifies the audience that this ID token is intended for. It must be one of the OAuth 2.0 client IDs of your application.
     */
    @JsonProperty("aud")
    private String audience;
    /**
     * (always) The time the ID token was issued, represented in Unix time (integer seconds).
     */
    @JsonProperty("iat")
    private Long idIssuedAt;
    /**
     * (always) The time the ID token expires, represented in Unix time (integer seconds).
     */
    @JsonProperty("exp")
    private Long expires;
    /**
     * The hosted Google Apps domain of the user. Provided only if the user belongs to a hosted domain.
     */
    @JsonProperty("hd")
    private String hostedDomain;

    public String getIsssuer() {
        return isssuer;
    }

    public void setIsssuer(String isssuer) {
        this.isssuer = isssuer;
    }

    public String getAccessTokenHash() {
        return accessTokenHash;
    }

    public void setAccessTokenHash(String accessTokenHash) {
        this.accessTokenHash = accessTokenHash;
    }

    public Boolean getEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(String subscriber) {
        this.subscriber = subscriber;
    }

    public String getAuthorizedPresenter() {
        return authorizedPresenter;
    }

    public void setAuthorizedPresenter(String authorizedPresenter) {
        this.authorizedPresenter = authorizedPresenter;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    public Long getIdIssuedAt() {
        return idIssuedAt;
    }

    public void setIdIssuedAt(Long idIssuedAt) {
        this.idIssuedAt = idIssuedAt;
    }

    public Long getExpires() {
        return expires;
    }

    public void setExpires(Long expires) {
        this.expires = expires;
    }

    public String getHostedDomain() {
        return hostedDomain;
    }

    public void setHostedDomain(String hostedDomain) {
        this.hostedDomain = hostedDomain;
    }
}
