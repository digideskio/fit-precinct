package de.konqi.fitapi.db;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konqi on 17.08.2015.
 */
@Entity
public class WorkoutData {
    @Id
    private Long id = null;

    @Parent
    private Ref<Workout> workout;

    private String type;

    @Serialize(zip = true)
    List<DataSet> dataSet = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ref<Workout> getWorkout() {
        return workout;
    }

    public void setWorkout(Ref<Workout> workout) {
        this.workout = workout;
    }

    public List<DataSet> getDataSet() {
        return dataSet;
    }

    public void setDataSet(List<DataSet> dataSet) {
        this.dataSet = dataSet;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
