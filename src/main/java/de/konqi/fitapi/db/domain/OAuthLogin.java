package de.konqi.fitapi.db.domain;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import de.konqi.fitapi.db.domain.User;

/**
 * Created by konqi on 21.08.2015.
 */
@Entity
public class OAuthLogin {
    @Id
    String id;

    Ref<User> user;

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
}
