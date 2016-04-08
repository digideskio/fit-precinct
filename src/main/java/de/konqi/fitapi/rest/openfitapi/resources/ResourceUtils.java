package de.konqi.fitapi.rest.openfitapi.resources;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.primitives.Longs;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import de.konqi.fitapi.Constants;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.DataSet;
import de.konqi.fitapi.db.domain.WorkoutData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Created by konqi on 17.08.2015.
 */
public class ResourceUtils {
    private static final Logger logger = LoggerFactory.getLogger(ResourceUtils.class);

    private static final int MAX_SETS = Constants.MAX_SETS;

    /**
     * load specific workout of a user
     *
     * @param itemId
     * @param user
     * @return
     */
    public static ObjectNode loadWorkout(Long itemId, de.konqi.fitapi.db.domain.User user) {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        ObjectNode rootNode = nodeFactory.objectNode();

        List<Object> dbWorkoutData = OfyService.ofy().load().ancestor(Key.create(Key.create(user), de.konqi.fitapi.db.domain.Workout.class, itemId)).list();
        logger.info("Retrieved " + dbWorkoutData.size() + " entries from the database");
        Map<String, List<DataSet>> datasets = new HashMap<>();

        for (Object entity : dbWorkoutData) {
            if (entity instanceof WorkoutData) {
                WorkoutData workoutData = (WorkoutData) entity;
                logger.info("Adding data of type '" + workoutData.getType() + "'.");

                if (datasets.containsKey(workoutData.getType())) {
                    datasets.get(workoutData.getType()).addAll(workoutData.getDataSet());
                } else {
                    datasets.put(workoutData.getType(), workoutData.getDataSet());
                }
            } else if (entity instanceof de.konqi.fitapi.db.domain.Workout) {
                de.konqi.fitapi.db.domain.Workout workout = (de.konqi.fitapi.db.domain.Workout) entity;
                logger.info("Adding workout data.");

                rootNode.put("name", workout.getName());
                rootNode.put("type", workout.getType());
                rootNode.put("start_time", workout.getStartTime().toString());
                rootNode.put("sharing", workout.getSharing());
                rootNode.put("notes", workout.getNotes());

                rootNode.put("total_distance", workout.getData().get("total_distance"));
                rootNode.put("duration", workout.getData().get("duration"));
                rootNode.put("clock_duration", workout.getData().get("clockDuration"));
                rootNode.put("calories", workout.getData().get("calories"));
                rootNode.put("elevation_gain", workout.getData().get("elevationGain"));
                rootNode.put("elevation_loss", workout.getData().get("elevationLoss"));
                rootNode.put("avg_speed", workout.getData().get("speedAvg"));
                rootNode.put("max_speed", workout.getData().get("speedMax"));
                rootNode.put("avg_heartrate", workout.getData().get("heartrateAvg"));
                rootNode.put("max_heartrate", workout.getData().get("heartrateMax"));
                rootNode.put("avg_cadence", workout.getData().get("cadenceAvg"));
                rootNode.put("max_cadence", workout.getData().get("cadenceMax"));
                rootNode.put("avg_power", workout.getData().get("powerAvg"));
                rootNode.put("max_power", workout.getData().get("powerMax"));
            }
        }

        for (String type : datasets.keySet()) {
            List<DataSet> entries = datasets.get(type);
            Collections.sort(entries, new Comparator<DataSet>() {
                @Override
                public int compare(DataSet o1, DataSet o2) {
                    return Longs.compare(o1.getOffset(), o2.getOffset());
                }
            });

            ArrayNode arrayNode;
            if (rootNode.has(type)) {
                arrayNode = (ArrayNode) rootNode.get(type);
            } else {
                arrayNode = rootNode.putArray(type);
            }

            for (DataSet dataSet : entries) {
                arrayNode.add(dataSet.getOffset());
                if (dataSet.getData().length < 2) {
                    arrayNode.add(dataSet.getData()[0]);
                } else {
                    ArrayNode dataArray = arrayNode.addArray();
                    for (String s : dataSet.getData()) {
                        dataArray.add(s);
                    }
                }
            }
        }


        return rootNode;
    }

    /**
     * Store workout data for user
     *
     * @param workout
     * @param user
     * @return
     */
    public static Long storeWorkout(Workout workout, de.konqi.fitapi.db.domain.User user) {
        Stack<Object> workoutDataToAdd = new Stack<>();

        Key<de.konqi.fitapi.db.domain.Workout> workoutKey = OfyService.factory().allocateId(user, de.konqi.fitapi.db.domain.Workout.class);
        de.konqi.fitapi.db.domain.Workout dbWorkout = new de.konqi.fitapi.db.domain.Workout();
        dbWorkout.setUser(Ref.create(user));
        dbWorkout.setId(workoutKey.getId());

        dbWorkout.setName(workout.getName());
        dbWorkout.setNotes(workout.getNotes());
        dbWorkout.setSharing(workout.getSharing());
        dbWorkout.setStartTime(workout.getStart_time());
        dbWorkout.setType(workout.getType());

        dbWorkout.getData().put("totalDistance", workout.getTotal_distance());
        dbWorkout.getData().put("duration", workout.getDuration());
        dbWorkout.getData().put("clockDuration", workout.getClock_duration());
        dbWorkout.getData().put("calories", workout.getCalories());
        dbWorkout.getData().put("elevationGain", workout.getElevation_gain());
        dbWorkout.getData().put("elevationLoss", workout.getElevation_loss());
        dbWorkout.getData().put("speedAvg", workout.getAvg_speed());
        dbWorkout.getData().put("speedMax", workout.getMax_speed());
        dbWorkout.getData().put("heartrateAvg", workout.getAvg_heartrate());
        dbWorkout.getData().put("heartrateMax", workout.getMax_heartrate());
        dbWorkout.getData().put("cadenceAvg", workout.getAvg_cadence());
        dbWorkout.getData().put("cadenceMax", workout.getMax_cadence());
        dbWorkout.getData().put("powerAvg", workout.getAvg_power());
        dbWorkout.getData().put("powerMax", workout.getMax_power());

        workoutDataToAdd.push(dbWorkout);

        workoutDataToAdd.addAll(getWorkoutLocation(workoutKey, workout));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getDistance(), "distance"));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getHeartrate(), "heartrate"));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getCadence(), "cadence"));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getElevation(), "elevation"));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getPower(), "power"));
        workoutDataToAdd.addAll(getWorkoutData(workoutKey, workout.getSpeed(), "speed"));

        OfyService.ofy().save().entities(workoutDataToAdd).now();

        return workoutKey.getId();
    }

    public static List<WorkoutData> getWorkoutLocation(Key<de.konqi.fitapi.db.domain.Workout> workoutKey, Workout workout) {
        List<WorkoutData> datapkg = new ArrayList<>();
        if (workout.getLocation() != null) {
            List<DataSet> datasets = new ArrayList<>(MAX_SETS);

            for (int i = 0; i < workout.getLocation().length; i += 2) {
                JsonNode offset = workout.getLocation()[i];
                JsonNode value = workout.getLocation()[i + 1];

                DataSet dataSet = new DataSet();
                dataSet.setOffset(offset.asLong());
                dataSet.setData(new String[]{value.get(0).asText(), value.get(1).asText()});

                datasets.add(dataSet);
                if (datasets.size() > MAX_SETS - 1) {
                    WorkoutData workoutData = new WorkoutData();
                    workoutData.setWorkout(Ref.create(workoutKey));
                    workoutData.setType("location");
                    workoutData.setDataSet(datasets);
                    datapkg.add(workoutData);

                    datasets = new ArrayList<>(MAX_SETS);
                }
            }

            if (datasets.size() > 0) {
                WorkoutData workoutData = new WorkoutData();
                workoutData.setWorkout(Ref.create(workoutKey));
                workoutData.setType("location");
                workoutData.setDataSet(datasets);
                datapkg.add(workoutData);
            }

        }

        return datapkg;
    }

    public static List<WorkoutData> getWorkoutData(Key<de.konqi.fitapi.db.domain.Workout> workoutKey, String[] data, String dataName) {
        List<WorkoutData> datapkg = new ArrayList<>();
        if (data != null) {
            List<DataSet> datasets = new ArrayList<>(MAX_SETS);

            for (int i = 0; i < data.length; i += 2) {
                long offset = Long.parseLong(data[i]);
                String value = data[i + 1];

                DataSet dataSet = new DataSet();
                dataSet.setOffset(offset);
                dataSet.setData(new String[]{value});

                datasets.add(dataSet);
                if (datasets.size() > MAX_SETS - 1) {
                    WorkoutData workoutData = new WorkoutData();
                    workoutData.setWorkout(Ref.create(workoutKey));
                    workoutData.setType(dataName);
                    workoutData.setDataSet(datasets);
                    datapkg.add(workoutData);

                    datasets = new ArrayList<>(MAX_SETS);
                }
            }

            if (datasets.size() > 0) {
                WorkoutData workoutData = new WorkoutData();
                workoutData.setWorkout(Ref.create(workoutKey));
                workoutData.setType(dataName);
                workoutData.setDataSet(datasets);
                datapkg.add(workoutData);
            }

        }

        return datapkg;
    }
}
