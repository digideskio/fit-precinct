package de.konqi.fitapi.db.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konqi on 17.08.2015.
 */
@Entity
public class User implements java.security.Principal {
    @Id
    Long id;

    private List<String> roles = new ArrayList<>();

    @Override
    public String getName() {
        return null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
