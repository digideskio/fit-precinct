package de.konqi.fitapi.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by konqi on 16.08.2015.
 */
@Entity
@Data
public class Workout {
    @JsonIgnore
    @Parent
    private Ref<User> user;

    @Id
    private Long id = null;

    @Index
    Date startTime;
    @Index
    String type;
    String sharing;
    String name;
    String notes;
    Map<String, Double> data = new HashMap<>();
}
