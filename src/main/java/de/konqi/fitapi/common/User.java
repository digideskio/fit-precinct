package de.konqi.fitapi.common;

/**
 * Created by konqi on 19.08.2015.
 */
public class User extends de.konqi.fitapi.db.domain.User {
    public User(de.konqi.fitapi.db.domain.User user) {
        super(user);
    }

    public User(User user){
        super(user);
    }

    public User() {

    }
}
