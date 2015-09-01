package de.konqi.fitapi.db.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by konqi on 23.08.2015.
 */
@Entity
@Cache
public class Session {
    @Id
    private String id;
    private Ref<User> user;
    @Index
    private long expires;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ref<User> getUser() {
        return user;
    }

    public void setUser(Ref<User> user) {
        this.user = user;
    }

    public long getExpires() {
        return expires;
    }

    public void setExpires(long expires) {
        this.expires = expires;
    }
}
