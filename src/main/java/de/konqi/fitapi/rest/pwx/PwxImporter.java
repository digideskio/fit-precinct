package de.konqi.fitapi.rest.pwx;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import de.konqi.fitapi.common.pwx.PwxFile;
import de.konqi.fitapi.common.pwx.PwxParser;
import de.konqi.fitapi.common.pwx.PwxWorkoutConverter;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
import de.konqi.fitapi.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konqi on 08.04.2016.
 */
@Slf4j
public class PwxImporter {
    public static void importPwx(String userEmail, InputStream is) throws NoSuchFieldException, IllegalAccessException, XMLStreamException {
        User user = UserRepository.getUserByEmail(userEmail);
        if (user != null) {
            log.info("Userid for email '" + userEmail + "' is " + user.getId() + ".");

            log.info("Parsing file.");
            PwxParser pwxParser = new PwxParser();
            PwxFile pwxFile = pwxParser.parse(is);

            log.info("Converting to database format.");
            PwxWorkoutConverter pwxWorkoutConverter = new PwxWorkoutConverter(pwxFile);
            Workout workout = pwxWorkoutConverter.getWorkout();

            log.info("Saving.");
            // Allocate workout id
            Key<Workout> workoutKey = OfyService.factory().allocateId(user, Workout.class);
            workout.setUser(Ref.create(user));
            workout.setId(workoutKey.getId());
            log.info("The workout will have the id '" + workoutKey.toString() + "'.");

            HashMap<String, WorkoutData> samples = pwxWorkoutConverter.getSamples();
            List<Object> entitiesToSave = new ArrayList<>(samples.keySet().size()+1);

            for (WorkoutData value : samples.values()) {
                value.setWorkout(Ref.create(workoutKey));
                entitiesToSave.add(value);
            }

            entitiesToSave.add(workout);

            OfyService.ofy().save().entities(entitiesToSave);
            log.info("Workout saved.");
        } else {
            log.info("No user found for email '" + userEmail + "'.");
        }
    }
}
