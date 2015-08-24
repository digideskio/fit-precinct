package de.konqi.fitapi.auth;

import de.konqi.fitapi.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by konqi on 23.08.2015.
 */
public class OpenIdManager {
    private static final Logger logger = LoggerFactory.getLogger(OpenIdManager.class);

    static Map<String, OpenIdProvider> supportedOpenIdProvidersDiscoveryUrls = Collections.singletonMap("https://accounts.google.com/.well-known/openid-configuration", null);

    private static OpenIdDiscoveryDocument loadDisovery(String discoveryDocUrl) {
        URL url = null;
        try {
            url = new URL(discoveryDocUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            if (urlConnection.getResponseCode() < 300) {
                return Utils.jacksonObjectMapper.readValue(urlConnection.getInputStream(), OpenIdDiscoveryDocument.class);
            }
        } catch (IOException e) {
            logger.error("Unable to load discovery document for URL '" + discoveryDocUrl + "'.", e);
        }

        return null;
    }

    private static PublicKeysManager loadKeysManager(String certUrl){
        return new PublicKeysManager.Builder(Utils.urlFetchTransport, Utils.jacksonObjectMapper.getFactory(), certUrl).build();
    }

    public static OpenIdProvider getProvider(String provider){
        Set<String> openIdProviderUrls = supportedOpenIdProvidersDiscoveryUrls.keySet();
        String discoveryDocUrl = openIdProviderUrls.toArray(new String[openIdProviderUrls.size()])[0];
        OpenIdProvider openIdProvider = supportedOpenIdProvidersDiscoveryUrls.get(discoveryDocUrl);

        if(openIdProvider == null) {
            openIdProvider = new OpenIdProvider();
            openIdProvider.setDiscoveryDocument(loadDisovery(discoveryDocUrl));
            openIdProvider.setPublicKeysManager(loadKeysManager(openIdProvider.getDiscoveryDocument().getJwksUri()));

            supportedOpenIdProvidersDiscoveryUrls.put(discoveryDocUrl, openIdProvider);
        }

        return openIdProvider;
    }

    public static class OpenIdProvider {
        OpenIdDiscoveryDocument discoveryDocument;
        PublicKeysManager publicKeysManager;

        public OpenIdDiscoveryDocument getDiscoveryDocument() {
            return discoveryDocument;
        }

        public void setDiscoveryDocument(OpenIdDiscoveryDocument discoveryDocument) {
            this.discoveryDocument = discoveryDocument;
        }

        public PublicKeysManager getPublicKeysManager() {
            return publicKeysManager;
        }

        public void setPublicKeysManager(PublicKeysManager publicKeysManager) {
            this.publicKeysManager = publicKeysManager;
        }
    }

}
