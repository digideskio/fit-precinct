package de.konqi.fitapi.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Serialize;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konqi on 17.08.2015.
 */
@Entity
public @Data
class WorkoutData {
    @JsonIgnore
    @Parent
    private Ref<Workout> workout;

    @Id
    private Long id = null;

    private String type;

    @Serialize(zip = true)
    List<DataSet> dataSet = new ArrayList<>();
}
