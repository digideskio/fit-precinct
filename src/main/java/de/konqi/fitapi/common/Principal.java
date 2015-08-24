package de.konqi.fitapi.common;

/**
 * Created by konqi on 19.08.2015.
 */
public class Principal extends User {

    public Principal(){
        super();
    }

    public Principal(Principal principal) {
        super();
    }

    public Principal(User user){
        super(user);
    }

    public Principal(de.konqi.fitapi.db.domain.User user) {
        super(user);
    }
}
