package de.konqi.fitapi.common.fit;

import de.konqi.fitapi.Utils;
import de.konqi.fitapi.common.WorkoutConverter;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konqi on 08.05.2016.
 */
public class FitWorkoutConverter extends WorkoutConverter {
    private static final long FIT_TIME_OFFSET = 631065600000l;
    private final FitData fitData;

    public FitWorkoutConverter(FitData fitData) {
        this.fitData = fitData;
    }

    public HashMap<String, WorkoutData> getSamples() {
        Long startTime = (Long) fitData.getMeta().get(FitGlobalMessageNum.SESSION).get(FitSessionType.STARTTIME);
        HashMap<String, WorkoutData> dataMap = new HashMap<>();

        Map<Object, Map<Enum, Object>> activities = fitData.getData().get(FitGlobalMessageNum.RECORD);
        // pre-calculate factor for semicircle conversion
        double multipicator = new BigDecimal(180).divide(new BigDecimal(2).pow(31), 20, RoundingMode.HALF_UP).doubleValue();

        for (Object ts : activities.keySet()) {
            Map<Enum, Object> datapoint = activities.get(ts);
            // calculate offset
            Long offset = (Long) ts - startTime;

            // convert GPS semicircles to decimal degrees
            Object a = datapoint.get(FitRecordType.LATITUDE);
            Object b = datapoint.get(FitRecordType.LONGITUDE);
            if (a != null && b != null) {
                double latitude = Utils.toLong(a) * multipicator;
                double longitude = Utils.toLong(b) * multipicator;
                put(dataMap, "location", offset, Double.toString(latitude), Double.toString(longitude));
            }

            a = datapoint.get(FitRecordType.SPEED);
            if (a != null) {
                put(dataMap, "speed", offset, Double.toString(Utils.toLong(a) / 1000));
            }

            a = datapoint.get(FitRecordType.DISTANCE);
            if (a != null) {
                put(dataMap, "distance", offset, Double.toString(Utils.toLong(a) / 100));
            }

            a = datapoint.get(FitRecordType.ALTITUDE);
            if (a != null) {
                put(dataMap, "elevation", offset, Double.toString((Utils.toLong(a) / 5) - 500));
            }

            a = datapoint.get(FitRecordType.HEARTRATE);
            if (a != null) {
                put(dataMap, "heartrate", offset, Long.toString(Utils.toLong(a)));
            }

            a = datapoint.get(FitRecordType.CADENCE);
            if (a != null) {
                put(dataMap, "cadence", offset, Long.toString(Utils.toLong(a)));
            }
        }

        return dataMap;
    }

    @Override
    public Workout getWorkout() {
        Workout workout = new Workout();

        Map<Enum, Object> sessionData = fitData.getMeta().get(FitGlobalMessageNum.SESSION);

        long startTime = (long) sessionData.get(FitSessionType.STARTTIME);
        workout.setStartTime(new Date(startTime * 1000 + FIT_TIME_OFFSET));

        FitSport sport = FitSport.getByValue((Integer) sessionData.get(FitSessionType.SPORT));
        workout.setType(sport.toString().toLowerCase());

//        sessionData.get(FitSessionType.EVENT);
//        sessionData.get(FitSessionType.EVENTTYPE);

        Object val = sessionData.get(FitSessionType.AVGHR);
        if (val != null) {
            workout.getData().put("heartrateAvg", (double) Utils.toLong(val));
        }
        val = sessionData.get(FitSessionType.MINHR);
        if (val != null) {
            workout.getData().put("heartrateMin", (double) Utils.toLong(val));
        }
        val = sessionData.get(FitSessionType.MAXHR);
        if (val != null) {
            workout.getData().put("heartrateMax", (double) Utils.toLong(val));
        }

        val = sessionData.get(FitSessionType.AVGSPEED);
        if (val != null) {
            workout.getData().put("speedAvg", (double) (Utils.toLong(val) / 1000));
        }
        val = sessionData.get(FitSessionType.MAXSPEED);
        if (val != null) {
            workout.getData().put("speedMax", (double) (Utils.toLong(val) / 1000));
        }

        val = sessionData.get(FitSessionType.AVGCADENCE);
        if (val != null) {
            workout.getData().put("cadenceAvg", (double) Utils.toLong(val));
        }

        val = sessionData.get(FitSessionType.ASCENT);
        if (val != null) {
            workout.getData().put("elevationGain", (double) Utils.toLong(val));
        }
        val = sessionData.get(FitSessionType.DECENT);
        if (val != null) {
            workout.getData().put("elevationLoss", (double) Utils.toLong(val));
        }


        val = sessionData.get(FitSessionType.TOTALTIMERTIME);
        if (val != null) {
            workout.getData().put("clockDuration", (double) (Utils.toLong(val) / 1000));
        }

//        workout.getData().put("totalDistance", summary.getDist());

        val = sessionData.get(FitSessionType.AVGPOWER);
        if (val != null) {
            workout.getData().put("powerAvg", (double) Utils.toLong(val));
        }

        val = sessionData.get(FitSessionType.TOTALDISTANCE);
        if (val != null) {
                workout.getData().put("totalDistance", (double) (Utils.toLong(val) / 100));
        }

        if (fitData.getLaps().size() > 0) {
            Long totalDistance = 0L;
            Long avgSpeed = 0L;
//            Long ascent = 0L, descent = 0L;
            int avgCnt = 0;
            for (Map<Enum, Object> lap : fitData.getLaps()) {
                val = lap.get(FitLapType.TOTAL_DISTANCE);
                if (val != null) {
                    totalDistance += Utils.toLong(val);
                }

                val = lap.get(FitLapType.AVG_SPEED);
                if (val != null) {
                    avgSpeed += Utils.toLong(val);
                    avgCnt++;
                }

//                val = lap.get(FitLapType.ASCENT);
//                if(val != null){
//                    ascent += (Integer)val;
//                }
//
//                val = lap.get(FitLapType.DESCENT);
//                if(val != null){
//                    descent += (Integer)val;
//                }
            }

            if (workout.getData().get("totalDistance") == null)
                workout.getData().put("totalDistance", (double) (totalDistance / 100));

            if (workout.getData().get("speedAvg") == null)
                workout.getData().put("speedAvg", (double) avgSpeed / (avgCnt * 1000));
        }

        return workout;
    }
}
