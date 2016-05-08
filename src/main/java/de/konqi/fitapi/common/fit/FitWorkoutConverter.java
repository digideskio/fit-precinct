package de.konqi.fitapi.common.fit;

import de.konqi.fitapi.common.WorkoutConverter;
import de.konqi.fitapi.db.domain.WorkoutData;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konqi on 08.05.2016.
 */
public class FitWorkoutConverter extends WorkoutConverter {
    private final FitData fitData;

    public FitWorkoutConverter(FitData fitData){
        this.fitData = fitData;
    }

    public HashMap<String, WorkoutData> getSamples() {
        Long startTime = (Long)fitData.getMeta().get(FitGlobalMessageNum.SESSION).get(FitSessionType.STARTTIME);
        HashMap<String, WorkoutData> dataMap = new HashMap<>();

        Map<Object, Map<Enum, Object>> activities = fitData.getData().get(FitGlobalMessageNum.RECORD);
        // pre-calculate factor for semicircle conversion
        double multipicator = new BigDecimal(180).divide(new BigDecimal(2).pow(31), 20, RoundingMode.HALF_UP).doubleValue();

        for (Object ts : activities.keySet()) {
            Map<Enum, Object> datapoint = activities.get(ts);
            // calculate offset
            Long offset = (Long)ts - startTime;

            // convert GPS semicircles to decimal degrees
            Object a = datapoint.get(FitRecordType.LATITUDE);
            Object b = datapoint.get(FitRecordType.LONGITUDE);
            if(a != null && b != null) {
                double latitude = ((Integer) a) * multipicator;
                double longitude = ((Integer) b) * multipicator;
                put(dataMap, "location", offset, Double.toString(latitude), Double.toString(longitude));
            }

            a = datapoint.get(FitRecordType.DISTANCE);
            if(a != null){
                put(dataMap, "distance", offset, Long.toString((long)a));
            }
            a = datapoint.get(FitRecordType.ALTITUDE);
            if(a != null){
                put(dataMap, "elevation", offset, Integer.toString((int)a));
            }
            a = datapoint.get(FitRecordType.HEARTRATE);
            if(a != null){
                put(dataMap, "heartrate", offset, Integer.toString((int)a));
            }
            a = datapoint.get(FitRecordType.CADENCE);
            if(a != null){
                put(dataMap, "cadence", offset, Integer.toString((int)a));
            }
        }

        return dataMap;
    }
}
