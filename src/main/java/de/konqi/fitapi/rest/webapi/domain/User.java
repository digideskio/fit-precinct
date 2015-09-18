package de.konqi.fitapi.rest.webapi.domain;

/**
 * Created by konqi on 18.09.2015.
 */
public class User extends de.konqi.fitapi.db.domain.User {
    public User(){

    }

    public User(de.konqi.fitapi.db.domain.User user){
        super(user);
    }

    String profileImg;

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }
}
