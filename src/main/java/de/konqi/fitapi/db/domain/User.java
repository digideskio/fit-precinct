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
    private Long id;
    private String name;
    private String email;
    private List<String> roles = new ArrayList<>();
    private String uploadPassword;

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.roles = user.roles;
    }

    public User() {

    }

    @Override
    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUploadPassword(String uploadPassword) {
        this.uploadPassword = uploadPassword;
    }

    public String getUploadPassword() {
        return uploadPassword;
    }
}
