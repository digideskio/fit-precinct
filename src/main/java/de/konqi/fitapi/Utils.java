package de.konqi.fitapi;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * Created by konqi on 19.08.2015.
 */
public class Utils {
    public static final HttpTransport urlFetchTransport = UrlFetchTransport.getDefaultInstance();
    public static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
}
