package de.konqi.fitapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;

import java.math.BigDecimal;

/**
 * Created by konqi on 19.08.2015.
 */
public class Utils {
    public static final HttpTransport urlFetchTransport = UrlFetchTransport.getDefaultInstance();
    public static final JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
    public static final ObjectMapper jacksonObjectMapper = new ObjectMapper();
    public static final MemcacheService memcacheService = MemcacheServiceFactory.getMemcacheService();
    public static final BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

    public static long toLong(Object o){
        if(o instanceof Integer){
            return (int)o;
        }
        else if(o instanceof Long){
            return (long)o;
        }
        else {
            throw new IllegalArgumentException("Cannot handle type " + o.getClass().getSimpleName());
        }
    }

}
