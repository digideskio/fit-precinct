package de.konqi.fitapi.common;

import de.konqi.fitapi.db.domain.DataSet;
import de.konqi.fitapi.db.domain.WorkoutData;

import java.util.HashMap;

/**
 * Created by konqi on 08.05.2016.
 */
public abstract class WorkoutConverter {
    protected void put(HashMap<String, WorkoutData> dataMap, String type, Long offset, String... data) {
        if (data == null) return;

        if (!dataMap.containsKey(type)) {
            WorkoutData workoutData = new WorkoutData();
            workoutData.setType(type);
            dataMap.put(type, workoutData);
        }

        DataSet dataSet = new DataSet();
        dataSet.setOffset(offset);
        dataSet.setData(data);
        dataMap.get(type).getDataSet().add(dataSet);
    }

    abstract public HashMap<String, WorkoutData> getSamples();
}
