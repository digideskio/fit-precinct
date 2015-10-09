package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.LoadResult;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.cmd.Query;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

/**
 * Repository class for accessing the databse / datastore
 *
 * @author konqi
 */
public class WorkoutRepository {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutRepository.class);

    /**
     * Loads all workout headers for a user (this will not work indefinably)
     * FIXME Introduce Paging at some point
     * @param user
     * @return
     */
    public static List<Workout> getWorkoutListForUser(User user) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).list();
    }

    /**
     * Retrieves the latest numWorkouts workout headers for a user
     * @param user
     * @param numWorkouts
     * @return
     */
    public static List<Workout> getLastWorkoutsForUser(User user, int numWorkouts) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).order("-startTime").limit(numWorkouts).list();
    }


    /**
     * Retrieves all workout headers for a user for a given type (Could be useful for filtering in the web frontend)
     * @param user
     * @param type
     * @return
     */
    public static List<Workout> getWorkoutListForUser(User user, String type) {
        return OfyService.ofy().load().type(Workout.class).ancestor(Ref.create(user)).filter("type", type).list();
    }

    /**
     * Retrieves a list of workout headers for a user in between since and until
     *
     * @param user
     * @param since
     * @param until
     * @return
     */
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

    /**
     * Retrieves all data associated with a user's workout (includes recorded data and workout header)
     *
     * @param webApiUser
     * @param workoutId
     * @return
     */
    public static List<Object> getWorkoutForUser(WebApiUser webApiUser, Long workoutId) {
        return OfyService.ofy().load().ancestor(Key.create(Key.create(webApiUser), Workout.class, workoutId)).list();
    }

    /**
     * Retrieves a workout header for a given user
     *
     * @param webApiUser
     * @param workoutId
     * @return
     */
    public static Workout getWorkoutHeadForUser(WebApiUser webApiUser, Long workoutId) {
        return OfyService.ofy().load().key(Key.create(Key.create(webApiUser), Workout.class, workoutId)).now();
    }

    /**
     * Updates the header information of a workout
     *
     * @param webApiUser
     * @param workout
     * @return
     */
    public static boolean updateWorkoutHeadForUser(WebApiUser webApiUser, Workout workout) {
        if(workout.getUser().getKey().getId() == webApiUser.getId()){
            OfyService.ofy().save().entity(workout).now();
            return true;
        }

        return false;
    }

    /**
     * Deletes a user's workout by the workout id
     *
     * @param user
     * @param workoutId
     * @return
     */
    public static boolean deleteWorkoutForUser(User user, Long workoutId) {
        OfyService.ofy().delete().key(Key.create(Key.create(user), Workout.class, workoutId)).now();
        return true;
    }

    /**
     * Creates a new workout header in the database (Used for manually creating workouts)
     * @param user
     * @param workout
     * @return
     */
    public static Workout insertHead(User user, Workout workout) {
        // Overwrite workout owner if falsely provided
        workout.setUser(Ref.create(user));
        // Let datastore auto assign an id
        workout.setId(null);

        Key<Workout> workoutKey = OfyService.ofy().save().entity(workout).now();
        workout.setId(workoutKey.getId());

        return workout;
    }

    /**
     * Appends data to an existing workout (Does not care whether the data is valid)
     *
     * @param webApiUser
     * @param workoutId
     * @param data
     * @return
     */
    public static boolean appendData(WebApiUser webApiUser, Long workoutId, WorkoutData data) {
        // Make sure the data is basically valid
        if(data.getType() == null ||data.getType().isEmpty()){
            return false;
        }
        if(data.getDataSet() == null || data.getDataSet().size() == 0){
            return false;
        }

        // Make sure that the workout exists and the current user is the owner
        Key<Workout> workoutKey = Key.create(Key.create(webApiUser), Workout.class, workoutId);
        Workout workout = OfyService.ofy().load().key(workoutKey).now();
        if(workout != null) {
            // Workout exists
            data.setWorkout(Ref.create(workoutKey));
            // Let datastore auto assign an id
            data.setId(null);

            Key<WorkoutData> dataKey = OfyService.ofy().save().entity(data).now();
            if(dataKey != null)
                return true;
        }

        return false;
    }
}
