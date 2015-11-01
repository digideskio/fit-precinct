package de.konqi.fitapi.db.repository;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import de.konqi.fitapi.Constants;
import de.konqi.fitapi.common.HashBuilder;
import de.konqi.fitapi.db.OfyService;
import de.konqi.fitapi.db.domain.Workout;
import de.konqi.fitapi.db.domain.WorkoutShare;
import de.konqi.fitapi.rest.webapi.WebApiUser;

import java.util.Date;
import java.util.List;

/**
 * Repository for creating and retrieving shared workout information
 *
 * @author konqi
 */
public class ShareWorkoutRepository {
    /**
     * Creates a share link in the database
     * @param webApiUser user the workout belongs to
     * @param workoutId id of workout to share
     * @param passphrase passphrase to access the share
     * @return link to be shared
     */
    public static String createShareLink(WebApiUser webApiUser, Long workoutId, Date validUntil, String passphrase){
        // Retrieve the header for the given workout as a precaution
        Workout workoutHeadForUser = WorkoutRepository.getWorkoutHeadForUser(webApiUser, workoutId);

        String shareLink = HashBuilder.SHA256.digest(webApiUser.getId().toString() + workoutId.toString() + Constants.SALT).asBase64();

        // Insert share link in database
        WorkoutShare workoutShare = new WorkoutShare();
        workoutShare.setShareLink(shareLink);
        workoutShare.setShareOwner(workoutHeadForUser.getUser());
        workoutShare.setSharedWorkout(Ref.create(workoutHeadForUser));
        workoutShare.setValidUntil(validUntil);
        workoutShare.setPassphrase(passphrase);

        OfyService.ofy().save().entity(workoutShare);

        return shareLink;
    }

    /**
     * Gets the header information of a shared workout
     * @param shareLink link to the share
     * @param passphrase passphrase to access the share
     * @return workout header information
     */
    public static Workout getWorkoutHeader(String shareLink, String passphrase){
        WorkoutShare workoutShare = OfyService.ofy().load().key(Key.create(WorkoutShare.class, shareLink)).now();
        // Check validity if set
        if(workoutShare.getValidUntil() != null){
            if(workoutShare.getValidUntil().before(new Date())){
                return null;
            }
        }

        // Check passphrase if set
        if(workoutShare.getPassphrase() != null){
            if(!workoutShare.getPassphrase().equals(passphrase)){
                return null;
            }
        }

        return workoutShare.getSharedWorkout().get();
    }

    /**
     * Gets workout data of shared workout
     * @param shareLink link to the share
     * @param passphrase passphrase to access the share
     * @return list of datasets of the shared workout
     */
    public static List<Object> getWorkoutData(String shareLink, String passphrase){
        WorkoutShare workoutShare = OfyService.ofy().load().key(Key.create(WorkoutShare.class, shareLink)).now();
        // Check validity if set
        if(workoutShare.getValidUntil() != null){
            if(workoutShare.getValidUntil().before(new Date())){
                return null;
            }
        }

        // Check passphrase if set
        if(workoutShare.getPassphrase() != null){
            if(!workoutShare.getPassphrase().equals(passphrase)){
                return null;
            }
        }

        return OfyService.ofy().load().ancestor(workoutShare.getSharedWorkout()).list();
    }
}
