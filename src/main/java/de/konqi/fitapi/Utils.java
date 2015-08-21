package de.konqi.fitapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

/**
 * Created by konqi on 19.08.2015.
 */
public class Utils {
    public static final HttpTransport urlFetchTransport = UrlFetchTransport.getDefaultInstance();
    public static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    public static final ObjectMapper jacksonObjectMapper = new ObjectMapper();
    public static final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
}
