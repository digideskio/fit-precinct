package de.konqi.fitapi.db.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import lombok.Data;

import java.util.Date;

/**
 * Created by konqi on 24.10.2015.
 */
@Entity
public @Data
class WorkoutShare {
    @Id
    String shareLink;
    Date validUntil;
    @Index
    Ref<User> shareOwner;
    Ref<Workout> sharedWorkout;
    String passphrase;
}
