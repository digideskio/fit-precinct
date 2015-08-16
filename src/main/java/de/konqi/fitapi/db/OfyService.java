package de.konqi.fitapi.db;

import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

/**
 * Created by konqi on 16.08.2015.
 */
public class OfyService {
    static ObjectifyFactory objectifyFactory;
    static {
        objectifyFactory = new ObjectifyFactory();
        objectifyFactory.register(Workout.class);
    }

    static ObjectifyService ofy() {
        return new ObjectifyService();
    }

    static ObjectifyFactory factory() {
        return objectifyFactory;
    }
}
