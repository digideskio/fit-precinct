package de.konqi.fitapi.db.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Objectify entity for users
 *
 * @author konqi
 */
@Entity
public class User implements java.security.Principal {
    @Id
    private Long id;
    private String name;
    @Index
    private String email;
    private Map<String, String> profileData = null; // new HashMap<>();
    @JsonIgnore
    private List<String> roles = new ArrayList<>();

    @JsonIgnore
    private String profileImgBlobKey;

    public User(User user) {
        this.id = user.id;
        this.name = user.name;
        this.email = user.email;
        this.roles = user.roles;
        this.profileImgBlobKey = user.profileImgBlobKey;
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

    public String getProfileImgBlobKey() {
        return profileImgBlobKey;
    }

    public void setProfileImgBlobKey(String profileImgBlobKey) {
        this.profileImgBlobKey = profileImgBlobKey;
    }

    public Map<String, String> getProfileData() {
        return profileData;
    }

    public void setProfileData(Map<String, String> profileData) {
        this.profileData = profileData;
    }
}
