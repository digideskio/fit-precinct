package de.konqi.fitapi;

import com.google.appengine.api.utils.SystemProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AppEngine environment variables
 *
 * @author konqi
 */
public class AppengineEnv {
    private static final Logger logger = LoggerFactory.getLogger(AppengineEnv.class);

    // private static AppIdentityService appIdentity = AppIdentityServiceFactory.getAppIdentityService();

    /**
     * Whether this is a development server or production environment
     */
    public static final boolean DEBUG = (SystemProperty.environment.value() !=
            SystemProperty.Environment.Value.Production);

    /**
     * AppId this application is running under (works in production environment)
     */
    public static final String APP_ID = SystemProperty.applicationId.get();

    /**
     * The mail domain for this application
     */
    public static final String MAIL_DOMAIN = AppengineEnv.APP_ID + ".appspotmail.com";
}