package de.konqi.fitapi.db.repository;

import de.konqi.fitapi.common.HashBuilder;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.User;

/**
 * Created by konqi on 23.08.2015.
 */
public class UserRepository {
    public static boolean setUploadPassword(User user, String uploadPassword){
        User dbUser = OfyService.ofy().load().entity(user).now();
        dbUser.setUploadPassword(HashBuilder.MD5.digest(uploadPassword).asHex());
        OfyService.ofy().save().entity(dbUser).now();

        return true;
    }
}
