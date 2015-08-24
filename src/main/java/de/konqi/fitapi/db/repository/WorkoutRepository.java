package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Ref;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.rest.webapi.resource.Workout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
public class WorkoutRepository {
    private static final Logger logger = LoggerFactory.getLogger(WorkoutRepository.class);

    public static List<Workout> getWorkoutListForUser(User user){
        return OfyService.ofy().load().type(Workout.class).filter("user", Ref.create(user)).list();
    }
}
