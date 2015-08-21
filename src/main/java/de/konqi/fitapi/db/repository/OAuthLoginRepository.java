package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.OAuthLogin;
import de.konqi.fitapi.db.domain.User;

/**
 * Created by konqi on 21.08.2015.
 */
public class OAuthLoginRepository {
    private static String createUserId(String issuer, String subscriber){
        return issuer + "_" + subscriber;
    }

    public static Ref<User> getLoginUser(String issuer, String subscriber){
        Key<OAuthLogin> loginKey = Key.create(OAuthLogin.class, createUserId(issuer, subscriber));
        OAuthLogin login = OfyService.ofy().load().key(loginKey).now();
        if(login != null) return login.getUser();
        return null;
    }

    public static User createUser(final String issuer, final String subscriber){
        return OfyService.ofy().transact(new Work<User>() {
            @Override
            public User run() {
                String userId = createUserId(issuer, subscriber);
                Key<OAuthLogin> loginKey = Key.create(OAuthLogin.class, createUserId(issuer, subscriber));
                OAuthLogin login = OfyService.ofy().load().key(loginKey).now();

                if(login == null){
                    OAuthLogin oAuthLogin = new OAuthLogin();
                    oAuthLogin.setId(userId);
                    oAuthLogin.setUser(Ref.create(OfyService.factory().allocateId(User.class)));

                    User user = new User();
                    user.setId(oAuthLogin.getUser().getKey().getId());

                    OfyService.ofy().save().entities(oAuthLogin, user).now();
                    return user;
                }

                return login.getUser().get();
            }
        });

    }
}
