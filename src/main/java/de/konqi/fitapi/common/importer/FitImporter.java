package de.konqi.fitapi.common.importer;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import de.konqi.fitapi.common.fit.FitData;
import de.konqi.fitapi.common.fit.FitHeader;
import de.konqi.fitapi.common.fit.FitParser;
import de.konqi.fitapi.common.fit.FitWorkoutConverter;
import de.konqi.fitapi.common.pwx.PwxWorkoutConverter;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;
import de.konqi.fitapi.db.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by konqi on 08.05.2016.
 */
@Slf4j
public class FitImporter implements Importer {
    public void importFromStream(String userEmail, InputStream is) throws IOException {
        User user = UserRepository.getUserByEmail(userEmail);
        if (user != null) {
            log.info("Userid for email '" + userEmail + "' is " + user.getId() + ".");

            log.info("Parsing file.");
            FitParser fitParser = new FitParser();
            FitHeader fitHeader = fitParser.parseHeader(is);
            FitData fitData = fitParser.readRecords(is, fitHeader.getDataSize());

            log.info("Converting to database format.");
            FitWorkoutConverter fitWorkoutConverter = new FitWorkoutConverter(fitData);
            Workout workout = fitWorkoutConverter.getWorkout();

            log.info("Saving.");
            // Allocate workout id
            Key<Workout> workoutKey = OfyService.factory().allocateId(user, Workout.class);
            workout.setUser(Ref.create(user));
            workout.setId(workoutKey.getId());
            log.info("The workout will have the id '" + workoutKey.toString() + "'.");

            HashMap<String, WorkoutData> samples = fitWorkoutConverter.getSamples();
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
