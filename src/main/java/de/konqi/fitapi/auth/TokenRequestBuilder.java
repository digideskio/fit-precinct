package de.konqi.fitapi.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konqi on 21.08.2015.
 */
public class TokenRequestBuilder {
    public static final Map<String, String> OPEN_ID_PROVIDER_TOKEN_BASE;

    static {
        OPEN_ID_PROVIDER_TOKEN_BASE = new HashMap<>();
        OPEN_ID_PROVIDER_TOKEN_BASE.put("google", "https://www.googleapis.com/oauth2/v3/token");
    }

    private String base;
    private String code;
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String grantType;

    private TokenRequestBuilder(String providerName) {
        String provider = providerName.toLowerCase();
        if (OPEN_ID_PROVIDER_TOKEN_BASE.containsKey(provider))
            this.base = OPEN_ID_PROVIDER_TOKEN_BASE.get(provider);
    }

    public static TokenRequestBuilder Builder(String providerName) {
        TokenRequestBuilder tokenUrlBuilder = new TokenRequestBuilder(providerName);

        if (tokenUrlBuilder.base == null) return null;

        return tokenUrlBuilder;
    }

    /**
     * The authorization code that is returned from the initial request.
     *
     * @param code
     * @return
     */
    public TokenRequestBuilder withCode(String code) {
        this.code = code;
        return this;
    }

    /**
     * The client ID that you obtain from the Developers Console, as described in Obtain OAuth 2.0 credentials.
     *
     * @param clientId
     * @return
     */
    public TokenRequestBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * The client secret that you obtain from the Developers Console, as described in Obtain OAuth 2.0 credentials.
     *
     * @param clientSecret
     * @return
     */
    public TokenRequestBuilder withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * The URI that you specify in the Developers Console, as described in Set a redirect URI.
     *
     * @param redirectUri
     * @return
     */
    public TokenRequestBuilder withRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    /**
     * This field must contain a value of authorization_code, as defined in the OAuth 2.0 specification.
     *
     * @param grantType
     * @return
     */
    public TokenRequestBuilder withGrantType(String grantType) {
        this.grantType = grantType;
        return this;
    }

    private String urlEncode(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return param;
        }
    }

    public String build() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("code=").append(code)
                .append("&client_id=").append(clientId)
                .append("&client_secret=").append(clientSecret)
                .append("&redirect_uri=").append(urlEncode(redirectUri))
                .append("&grant_type=").append(grantType);

        return sb.toString();
    }

    public String getBase(){
        return base;
    }
}
