package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.Work;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.Session;
import de.konqi.fitapi.db.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Repository for active web sessions
 *
 * @author konqi
 */
public class SessionRepository {
    private static final Logger logger = LoggerFactory.getLogger(SessionRepository.class);
    private static final long SESSION_EXPIRATION_SECONDS = 60 * 60 * 24 * 7;

    /**
     *
     * @param user
     * @return
     */
    public static String createSession(final User user) {
        return createSession(Ref.create(user));
    }

    /**
     *
     * @param userRef
     * @return
     */
    public static String createSession(final Ref<User> userRef) {
        assert userRef != null;

        Session session;
        do {
            session = OfyService.ofy().transact(new Work<Session>() {
                @Override
                public Session run() {
                    Session session = new Session();
                    session.setId(new BigInteger(130, new SecureRandom()).toString(32));
                    session.setUser(userRef);
                    session.setExpires(System.currentTimeMillis() / 1000 + SESSION_EXPIRATION_SECONDS);

                    if (OfyService.ofy().load().entity(session).now() == null) {
                        OfyService.ofy().save().entity(session).now();
                        return session;
                    }

                    return null;
                }
            });
        } while (session == null);

        return session.getId();
    }

    /**
     * Gets the user for a valid session id
     * @param sessionId id of the session
     * @return authorized user for session
     */
    public static User getSession(String sessionId) {
        Session session = OfyService.ofy().load().key(Key.create(Session.class, sessionId)).now();
        if (session == null) return null;
        if (session.getExpires() < System.currentTimeMillis() / 1000) {
            logger.info("Session '" + sessionId + "' expired.");
            return null;
        }

        return session.getUser().get();
    }
}
