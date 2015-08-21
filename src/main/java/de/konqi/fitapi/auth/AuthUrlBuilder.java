package de.konqi.fitapi.auth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Auth URL builder for oauth2
 *
 * @author konqi
 */
public class AuthUrlBuilder {
    public static final Map<String, String> OPEN_ID_PROVIDER_AUTH_BASE;

    static {
        OPEN_ID_PROVIDER_AUTH_BASE = new HashMap<>();
        OPEN_ID_PROVIDER_AUTH_BASE.put("google", "https://accounts.google.com/o/oauth2/auth");
    }

    private String base;
    private String clientId;
    private String responseType;
    private String scope;
    private String redirectUri;
    private String state;
    private String loginHint;
    private String openidRealm;
    private String hd;

    /**
     * Constructor
     *
     * @param providerName
     */
    private AuthUrlBuilder(String providerName) {
        String provider = providerName.toLowerCase();
        if (OPEN_ID_PROVIDER_AUTH_BASE.containsKey(provider))
            this.base = OPEN_ID_PROVIDER_AUTH_BASE.get(provider);
    }

    public static AuthUrlBuilder Builder(String providerName) {
        AuthUrlBuilder authUrlBuilder = new AuthUrlBuilder(providerName);

        if (authUrlBuilder.base == null) return null;

        return authUrlBuilder;
    }

    /**
     * which you obtain from the Developers Console
     *
     * @param clientId
     * @return
     */
    public AuthUrlBuilder withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * which in a basic request should be code
     *
     * @param responseType
     * @return
     */
    public AuthUrlBuilder withResponseType(String responseType) {
        this.responseType = responseType;
        return this;
    }

    /**
     * should be the HTTP endpoint on your server that will receive the response from Google. You specify this URI in the Developers Console.
     *
     * @param redirectUri
     * @return
     */
    public AuthUrlBuilder withRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    /**
     * which in a basic request should be openid email.
     *
     * @param scope
     * @return
     */
    public AuthUrlBuilder withScope(String scope) {
        this.scope = scope;
        return this;
    }

    /**
     * should include the value of the anti-forgery unique session token, as well as any other information needed to recover the context when the user returns to your application, e.g., the starting URL. (Read more at state.)
     *
     * @param state
     * @return
     */
    public AuthUrlBuilder withState(String state) {
        this.state = state;
        return this;
    }


    /**
     * can be the user's email address or the sub string, which is equivalent to the user's Google ID. If you do not provide a login_hint and the user is currently logged in, the consent screen includes a request for approval to release the user’s email address to your app. (Read more at login_hint.)
     *
     * @param loginHint
     * @return
     */
    public AuthUrlBuilder withLoginHint(String loginHint) {
        this.loginHint = loginHint;
        return this;
    }


    /**
     * Use the  openid.realm if you are migrating an existing application from OpenID 2.0 to OpenID Connect. For details, see Migrating off of OpenID 2.0.
     *
     * @param openidRealm
     * @return
     */
    public AuthUrlBuilder withOpenIdRealm(String openidRealm) {
        this.openidRealm = openidRealm;
        return this;
    }


    /**
     * Use the hd parameter to limit sign-in to a particular Google Apps hosted domain. (Read more at hd.)
     *
     * @param hd
     * @return
     */
    public AuthUrlBuilder withHd(String hd) {
        this.hd = hd;
        return this;
    }

    private String urlEncode(String param){
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return param;
        }
    }

    public String build() {
        StringBuilder sb = new StringBuilder(base);
        sb
                .append("?client_id=").append(clientId)
                .append("&response_type=").append(urlEncode(responseType))
                .append("&scope=").append(urlEncode(scope))
                .append("&redirect_uri=").append(urlEncode(redirectUri));

        if (state != null)
            sb.append("&state=").append(urlEncode(state));

        if (loginHint != null)
            sb.append("&login_hint=").append(urlEncode(loginHint));

        if (openidRealm != null)
            sb.append("&openid.realm=").append(urlEncode(openidRealm));

        if (hd != null)
            sb.append("&hd=").append(urlEncode(hd));

        return sb.toString();
    }

}
