package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
public class WorkoutRepository {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutRepository.class);

    public static List<Workout> getWorkoutListForUser(User user) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).list();
    }

    public static List<Object> getWorkoutForUser(WebApiUser webApiUser, Long workoutId) {
        return OfyService.ofy().load().ancestor(Key.create(Key.create(webApiUser), Workout.class, workoutId)).list();
    }
}
