package de.konqi.fitapi.db.repository;

import com.google.appengine.api.blobstore.BlobKey;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import de.konqi.fitapi.Utils;
import de.konqi.fitapi.common.HashBuilder;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.UploadCredential;
import de.konqi.fitapi.db.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by konqi on 23.08.2015.
 */
public class UserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public static boolean setUploadUser(final User user, final String username, final String uploadPassword) {
        UploadCredential uploadCredential = OfyService.ofy().transact(new Work<UploadCredential>() {
            @Override
            public UploadCredential run() {
                UploadCredential dbCurrentCredential = OfyService.ofy().load().key(Key.create(UploadCredential.class, username)).now();
                if (dbCurrentCredential == null) {
                    // No such username exists
                    UploadCredential uploadCredential = new UploadCredential();
                    uploadCredential.setUserId(username);
                    uploadCredential.setPassword(HashBuilder.SHA256.digest(uploadPassword).asHex());
                    uploadCredential.setUser(Ref.create(user));
                    OfyService.ofy().save().entity(uploadCredential).now();
                    return uploadCredential;
                } else if (dbCurrentCredential.getUserId().equals(user.getId())) {
                    dbCurrentCredential.setUserId(username);
                    dbCurrentCredential.setPassword(HashBuilder.SHA256.digest(uploadPassword).asHex());
                    dbCurrentCredential.setUser(Ref.create(user));
                    OfyService.ofy().save().entity(dbCurrentCredential).now();
                    return dbCurrentCredential;
                }
                logger.info("Upload username '" + username + "' is taken.");
                return null;
            }
        });

        return uploadCredential != null;
    }

    public static UploadCredential getUploadUserByUsername(String username){
        return OfyService.ofy().load().key(Key.create(UploadCredential.class, username)).now();
    }

    public static boolean updateUser(User user){
        User dbUser = OfyService.ofy().load().key(Key.create(User.class, user.getId())).safe();

        dbUser.setName(user.getName());
        dbUser.setProfileData(user.getProfileData());

        if(user.getProfileImgBlobKey() != null) {
            if (dbUser.getProfileImgBlobKey() != null) {
                Utils.blobstoreService.delete(new BlobKey(dbUser.getProfileImgBlobKey()));
            }
            dbUser.setProfileImgBlobKey(user.getProfileImgBlobKey());
        }
        // dbUser.setEmail(user.getEmail());

        Key<User> now = OfyService.ofy().save().entity(dbUser).now();
        return now != null;
    }

//    public static User getUserByEmail(String uploadUsername) {
//        List<User> users = OfyService.ofy().load().type(User.class).filter("uploadUsername", uploadUsername).list();
//        if (users.size() == 1) {
//            return users.get(0);
//        }
//
//        logger.error("Invalid number of users with the same username.");
//        return null;
//    }
}
