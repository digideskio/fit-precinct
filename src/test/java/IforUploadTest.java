import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.CookieStore;

/**
 * Created by konqi on 20.08.2015.
 */
public class IforUploadTest {
    private static final Logger Logger = LoggerFactory.getLogger(IforUploadTest.class);

    static class mOpenFitAipSite {

        public static String getSiteBaseUrl() {
            return null;
        }

        public static String getSiteName() {
            return null;
        }

        public static String getUserId() {
            return null;
        }

        public static String getUserPassword() {
            return null;
        }
    }

    static class R {
        static class string {
            public static String progress_logging_in;
            public static String error_no_internet;
            public static String error_bad_login;
            public static String error_bad_data;
            public static String error_responce_error;
            public static String progress_uploading;
        }
    }

    private String getString(String id){
        return null;
    }

    private void publishProgress(String string){

    }

//    private boolean sendOpenFitApi() {
//        final String HTTP_PREFIX = "http://";
//        final String LOGIN = "/openfitapi/api/user/login";
//        final String UPLOAD = "/openfitapi/api/fitnessActivities";
//        final String VIEW_ACTIVITY = "/activity/";
//        final String BASE_URL = HTTP_PREFIX + mOpenFitAipSite.getSiteBaseUrl();
//        final String SITE_NAME = mOpenFitAipSite.getSiteName();
//        String session_id = null;
//        String session_name = null;
//        String activity_id = null;
//        File json_fi = null;
//        Logger.info( "sendOpenFitApi :{} Using base address :{}" ,SITE_NAME , BASE_URL);
//
//        publishProgress(this.getString(R.string.progress_logging_in));
//        String login = "{\"username\":\"" + mOpenFitAipSite.getUserId() + "\",\n" + "\"password\":\""
//                + mOpenFitAipSite.getUserPassword() + "\"}";
//        String login_url = BASE_URL + LOGIN;
//        mResponse = mHttpHelper.PostData(login_url, login, "application/json", "application/json");
//        if (mHttpHelper.CheckResponseError(SITE_NAME + " Login", mResponse, false)) {
//            String page = mHttpHelper.getPageFromResponse(mResponse);
//            JSONObject login_resp = null;
//            try {
//                login_resp = new JSONObject(page);
//                session_id = login_resp.getString("sessid");
//                session_name = login_resp.getString("session_name");
//                Logger.trace("{} sessid :{} session_name :{}" ,SITE_NAME ,session_id ,session_name);
//                ok = ((session_id != null) && (session_id.length() > 0) && (session_name != null) && (session_name.length() > 0));
//            } catch (JSONException e) {
//                Logger.warn( "upload OpenFitApi login JSONArray error ", e);
//                Logger.warn( "page :{}", page);
//                String[] params = new String[1];
//                params[0] = "page :" + page;
//                AnaliticsWrapper.caughtExceptionHandeler(e, "Uploader", "OpenFitApi Login error", params);
//                ok = false;
//            }
//        } else {
//            ok = false;
//            if ((mResponse != null) && (mResponse.getStatusLine().getStatusCode() == 401)) {
//                // Authorizasion error
//                mUploadError = new SpannedString(getString(R.string.error_bad_login));
//            } else {
//                mUploadError = mHttpHelper.mLastError;
//                if ((mUploadError == null) || (mUploadError.length() == 0)) {
//                    mUploadError = new SpannedString(getString(R.string.error_no_internet));
//                }
//            }
//            return false;
//        }
//
//        if (ok) {
//            CookieStore cookies = mHttpHelper.getCookieStore();
//            Cookie cookie = new BasicClientCookie(session_name, session_id);
//            cookies.addCookie(cookie);
//
//            publishProgress(this.getString(R.string.progress_preparing));
//            File ipp_fi = mApp.GetLoggingFile(".ipp", mRideData.getFileName(), false);
//            IppActivity act = IppActivity.setRideHistory(ipp_fi, mUri, mApp);
//            json_fi = IpBikeApplication.GetNewTempFile(".json", mRideData.getUserFileName());
//            if ((act != null) && (json_fi != null)) {
//                ok = act.saveOpenFitApiJsonFile(json_fi, true, false);
//            } else {
//                ok = false;
//                Logger.warn( "upload {} saveOpenFitApiJsonFile error ", SITE_NAME);
//                AnaliticsWrapper.unexpectedNullHandeler("Uploader", "act or json_fi", "OpenFitApi write file", null);
//                mUploadError = new SpannedString(getString(R.string.error_bad_data));
//            }
//        }
//
//        if (ok) {
//            publishProgress(this.getString(R.string.progress_uploading));
//            String upload_url = BASE_URL + UPLOAD;
//            String content_type = "application/json";
//            String accept_type = "application/json";
//            mResponse = mHttpHelper.PostFileData(upload_url, content_type, json_fi, accept_type);
//            if (mHttpHelper.CheckResponseError(SITE_NAME + " UPLOAD", mResponse, false)) {
//                String page = mHttpHelper.getPageFromResponse(mResponse);
//                JSONObject upload_resp = null;
//                Logger.trace( "page :{}", page);
//
//                try {
//                    upload_resp = new JSONObject(page);
//                    JSONArray upload_array = upload_resp.getJSONArray("uris");
//                    String res_uri = upload_array.getString(0);
//                    String[] res_array = res_uri.split("/");
//                    activity_id = res_array[res_array.length - 1];
//                    Logger.trace("{} activity_id :{}",SITE_NAME , activity_id);
//                    ok = ((activity_id != null) && (activity_id.length() > 0));
//                } catch (JSONException e) {
//                    Logger.warn( "upload OpenFitApi activity_id JSONArray error ", e);
//                    Logger.warn( "page :{}", page);
//                    String[] params = new String[1];
//                    params[0] = "page :" + page;
//                    AnaliticsWrapper.caughtExceptionHandeler(e, "Uploader", "OpenFitApi activity_id error", params);
//                    ok = false;
//                    mUploadError = new SpannedString(getString(R.string.error_responce_error));
//                }
//            } else {
//                ok = false; // error logged in checker.
//                mUploadError = mHttpHelper.mLastError;
//                if ((mUploadError == null) || (mUploadError.length() == 0)) {
//                    mUploadError = new SpannedString(getString(R.string.error_responce_error));
//                }
//            }
//        }
//
//        if (ok) {
//            String res = BASE_URL + VIEW_ACTIVITY + activity_id;
//            mResUri = Uri.parse(res);
//            // mResUri = Uri.parse(VIEW_URL + activity_id);
//            if (!activity_id.equals("")) {
//                addUploadDetails(mRideData.getId(), SITE_NAME, res, activity_id);
//                return true;
//            }
//        }
//        return false;
//    }
}
