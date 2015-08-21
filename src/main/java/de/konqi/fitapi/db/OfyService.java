package de.konqi.fitapi.db;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import de.konqi.fitapi.db.domain.OAuthLogin;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;

/**
 * Created by konqi on 16.08.2015.
 */
public class OfyService {
    static {
        ObjectifyService.register(OAuthLogin.class);
        ObjectifyService.register(User.class);

        ObjectifyService.register(Workout.class);
        ObjectifyService.register(WorkoutData.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}
