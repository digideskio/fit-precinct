package de.konqi.fitapi.common.pwx;

import de.konqi.fitapi.common.WorkoutConverter;
import de.konqi.fitapi.db.domain.DataSet;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konqi on 08.04.2016.
 */
public class PwxWorkoutConverter extends WorkoutConverter {
    PwxFile pwxFile;

    public PwxWorkoutConverter(PwxFile pwxFile) {
        this.pwxFile = pwxFile;
    }


    public HashMap<String, WorkoutData> getSamples() {
        HashMap<String, WorkoutData> dataMap = new HashMap<>();

        for (Sample sample : pwxFile.getSamples()) {
            // cadence
            if (sample.getCad() != null) {
                put(dataMap, "cadence", sample.getTimeoffset(), sample.getCad().toString());
            }

            // elevation
            if (sample.getAlt() != null) {
                put(dataMap, "elevation", sample.getTimeoffset(), sample.getAlt().toString());
            }
            // distance
            if (sample.getDist() != null) {
                put(dataMap, "distance", sample.getTimeoffset(), sample.getDist().toString());
            }

            // heartrate
            if (sample.getHr() != null) {
                put(dataMap, "heartrate", sample.getTimeoffset(), sample.getHr().toString());
            }

            // location
            if (sample.getLat() != null && sample.getLon() != null) {
                put(dataMap, "location", sample.getTimeoffset(), sample.getLat().toString(), sample.getLon().toString());
            }
        }

        // TODO Handle overflow & splitting

        return dataMap;
    }

    final static Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("bike", "Cycling");
        typeMap.put("mountain bike", "Cycling");

        typeMap.put("run", "Running");
        typeMap.put("walk", "Walking");
        typeMap.put("race", "Running");

        typeMap.put("swim", "Other");
        typeMap.put("brick", "Other");
        typeMap.put("cross train", "Other");
        typeMap.put("day off", "Other");
        typeMap.put("strength", "Other");
        typeMap.put("xc ski", "Other");
        typeMap.put("rowing", "Other");
        typeMap.put("other", "Other");
    }

    public Workout getWorkout() {
        Workout workout = new Workout();

        Summary summary = pwxFile.getSummary();
        workout.setType(typeMap.get(summary.getSportType().toLowerCase()));
        workout.setStartTime(summary.getTime());

        workout.getData().put("heartrateAvg", summary.getHrAvg());
        workout.getData().put("heartrateMin", summary.getHrMin());
        workout.getData().put("heartrateMax", summary.getHrMax());
        workout.getData().put("speedAvg", summary.getSpdAvg());
        workout.getData().put("speedMin", summary.getSpdMin());
        workout.getData().put("speedMax", summary.getSpdMax());
        workout.getData().put("cadenceAvg", summary.getCad());
        workout.getData().put("elevationGain", summary.getClimbingelevation());
        workout.getData().put("elevationLoss", summary.getDescendingelevation());
        workout.getData().put("clockDuration", summary.getDuration());
        workout.getData().put("totalDistance", summary.getDist());
        workout.getData().put("powerAvg", summary.getPwr());

        return workout;
    }
}
