package de.konqi.fitapi.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
public class OpenIdDiscoveryDocument {
    @JsonProperty("issuer")
    String issuer;
    @JsonProperty("authorization_endpoint")
    String authorizationEndpoint;
    @JsonProperty("token_endpoint")
    String tokenEndpoint;
    @JsonProperty("userinfo_endpoint")
    String userinfoEndpoint;
    @JsonProperty("revocation_endpoint")
    String revocationEndpoint;
    @JsonProperty("jwks_uri")
    String jwksUri;
    @JsonProperty("response_types_supported")
    List<String> responseTypesSupported;
    @JsonProperty("subject_types_supported")
    List<String> subjectTypesSupported;
    @JsonProperty("id_token_signing_alg_values_supported")
    List<String> idTokenSigningAlgorithmValuesSupported;
    @JsonProperty("scopes_supported")
    List<String> scopesSupported;
    @JsonProperty("token_endpoint_auth_methods_supported")
    List<String> tokenEndpointAuthMethodsSupported;
    @JsonProperty("claims_supported")
    List<String> claimsSupported;

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getAuthorizationEndpoint() {
        return authorizationEndpoint;
    }

    public void setAuthorizationEndpoint(String authorizationEndpoint) {
        this.authorizationEndpoint = authorizationEndpoint;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public void setTokenEndpoint(String tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }

    public String getUserinfoEndpoint() {
        return userinfoEndpoint;
    }

    public void setUserinfoEndpoint(String userinfoEndpoint) {
        this.userinfoEndpoint = userinfoEndpoint;
    }

    public String getRevocationEndpoint() {
        return revocationEndpoint;
    }

    public void setRevocationEndpoint(String revocationEndpoint) {
        this.revocationEndpoint = revocationEndpoint;
    }

    public String getJwksUri() {
        return jwksUri;
    }

    public void setJwksUri(String jwksUri) {
        this.jwksUri = jwksUri;
    }

    public List<String> getResponseTypesSupported() {
        return responseTypesSupported;
    }

    public void setResponseTypesSupported(List<String> responseTypesSupported) {
        this.responseTypesSupported = responseTypesSupported;
    }

    public List<String> getSubjectTypesSupported() {
        return subjectTypesSupported;
    }

    public void setSubjectTypesSupported(List<String> subjectTypesSupported) {
        this.subjectTypesSupported = subjectTypesSupported;
    }

    public List<String> getIdTokenSigningAlgorithmValuesSupported() {
        return idTokenSigningAlgorithmValuesSupported;
    }

    public void setIdTokenSigningAlgorithmValuesSupported(List<String> idTokenSigningAlgorithmValuesSupported) {
        this.idTokenSigningAlgorithmValuesSupported = idTokenSigningAlgorithmValuesSupported;
    }

    public List<String> getScopesSupported() {
        return scopesSupported;
    }

    public void setScopesSupported(List<String> scopesSupported) {
        this.scopesSupported = scopesSupported;
    }

    public List<String> getTokenEndpointAuthMethodsSupported() {
        return tokenEndpointAuthMethodsSupported;
    }

    public void setTokenEndpointAuthMethodsSupported(List<String> tokenEndpointAuthMethodsSupported) {
        this.tokenEndpointAuthMethodsSupported = tokenEndpointAuthMethodsSupported;
    }

    public List<String> getClaimsSupported() {
        return claimsSupported;
    }

    public void setClaimsSupported(List<String> claimsSupported) {
        this.claimsSupported = claimsSupported;
    }
}
