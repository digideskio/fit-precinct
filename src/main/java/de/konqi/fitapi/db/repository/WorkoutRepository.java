package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
public class WorkoutRepository {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutRepository.class);

    public static List<Workout> getWorkoutListForUser(User user) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).list();
    }

    public static List<Workout> getLastWorkoutsForUser(User user, int numWorkouts) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).order("-startTime").limit(numWorkouts).list();
    }


    public static List<Workout> getWorkoutListForUser(User user, String type) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).filter("type", type).list();
    }

    public static List<Workout> getWorkoutListForUser(User user, Date since, Date until) {
        Query<Workout> query = OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user));
        if(since != null){
            query = query.filter("startTime >=", since);
        }
        if(until != null){
            query = query.filter("startTime <", until);
        }

        return query.list();
    }


    public static List<Object> getWorkoutForUser(WebApiUser webApiUser, Long workoutId) {
        return OfyService.ofy().load().ancestor(Key.create(Key.create(webApiUser), Workout.class, workoutId)).list();
    }

    public static Workout getWorkoutHeadForUser(WebApiUser webApiUser, Long workoutId) {
        return OfyService.ofy().load().key(Key.create(Key.create(webApiUser), Workout.class, workoutId)).now();
    }

    public static boolean updateWorkoutHeadForUser(WebApiUser webApiUser, Workout workout) {
        if(workout.getUser().getKey().getId() == webApiUser.getId()){
            OfyService.ofy().save().entity(workout).now();
            return true;
        }

        return false;
    }

    public static boolean deleteWorkoutForUser(User user, Long workoutId) {
        OfyService.ofy().delete().key(Key.create(Key.create(user), Workout.class, workoutId)).now();
        return true;
    }
}
