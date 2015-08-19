package de.konqi.fitapi.db;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Created by konqi on 17.08.2015.
 */
@Entity
public class User {
    @Id
    Long id;
}
