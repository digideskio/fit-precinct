package de.konqi.fitapi.rest.webapi.resource;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.UploadOptions;
import de.konqi.fitapi.AppengineEnv;
import de.konqi.fitapi.Constants;
import de.konqi.fitapi.Utils;
import de.konqi.fitapi.auth.*;
import de.konqi.fitapi.db.repository.OAuthLoginRepository;
import de.konqi.fitapi.db.repository.SessionRepository;
import de.konqi.fitapi.db.repository.UserRepository;
import de.konqi.fitapi.rest.webapi.WebApiUser;
import de.konqi.fitapi.rest.webapi.domain.LoginCallbackResponse;
import de.konqi.fitapi.rest.webapi.domain.User;
import org.apache.http.HttpStatus;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.server.mvc.Viewable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by konqi on 19.08.2015.
 */
@Path("/user")
@PermitAll
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {
    private static final Logger logger = LoggerFactory.getLogger(UserResource.class);

    @GET
    @Path("/me")
    @RolesAllowed("user")
    public Response me(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context SecurityContext sc) {
        Principal userPrincipal = sc.getUserPrincipal();
        User user = new User((de.konqi.fitapi.common.User) userPrincipal);
        StringBuffer requestURL = request.getRequestURL();
        String base = requestURL.substring(0, requestURL.indexOf("/", 8));
        user.setProfileImg(base + request.getServletPath() + "/img/" + user.getProfileImgBlobKey());

        return Response.ok().entity(user).build();
    }

    @GET
    @Path("/getUpdateUrl")
    @RolesAllowed("user")
    public Response getUpdateUrl(@Context HttpServletRequest request, @Context HttpServletResponse response, @Context SecurityContext sc) {
        UploadOptions uploadOptions = UploadOptions.Builder.withMaxUploadSizeBytes(500 * 1024).maxUploadSizeBytesPerBlob(500 * 1024);
        String uploadUrlStr = Utils.blobstoreService.createUploadUrl(request.getServletPath() + "/user/postUpdateUrl", uploadOptions);
        if (AppengineEnv.DEBUG) {
            uploadUrlStr = uploadUrlStr.replaceAll(System.getenv("COMPUTERNAME").toLowerCase(), "localhost");
        }

        Map<String, String> uploadUrl = Collections.singletonMap("updateUrl", uploadUrlStr);

        return Response.ok().entity(uploadUrl).build();
    }

    @POST
    @Path("/postUpdateUrl")
    @RolesAllowed("user")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response postUpdateUrl(FormDataMultiPart multiPart, @Context HttpServletRequest request, @Context HttpServletResponse response, @Context SecurityContext sc) {
        Map<String, List<BlobKey>> uploads = Utils.blobstoreService.getUploads(request);
        if (uploads.size() == 1) {
            for (String s : uploads.keySet()) {
                List<BlobKey> blobKeys = uploads.get(s);
                if (blobKeys.size() == 1) {
                    BlobKey blobKey = blobKeys.get(0);

                    try {
                        User user = Utils.jacksonObjectMapper.readValue(multiPart.getField("user").getValue(), User.class);
                        user.setProfileImgBlobKey(blobKey.getKeyString());

                        return updateProfile(user, request, response, sc);
                    } catch (IOException ignored) {
                    }
                }
            }
        }

        for (List<BlobKey> blobKeys : uploads.values()) {
            for (BlobKey blobKey : blobKeys) {
                Utils.blobstoreService.delete(blobKey);
            }
        }


        return Response.status(HttpStatus.SC_BAD_REQUEST).build();
    }

    @POST
    @Path("/me")
    @RolesAllowed("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProfile(User updateUser, @Context HttpServletRequest request, @Context HttpServletResponse response, @Context SecurityContext sc) {
        Principal userPrincipal = sc.getUserPrincipal();
        de.konqi.fitapi.common.User user = (de.konqi.fitapi.common.User) userPrincipal;

        // Override provided id (if any)
        updateUser.setId(user.getId());

        /*if(updateUser.getImgUrl() != null){
            blobstoreService.getUploads()
        }*/

        if (UserRepository.updateUser(updateUser)) {
            return me(request, response, sc);
        }

        return Response.status(HttpStatus.SC_UNAUTHORIZED).build();
    }


    @PUT
    @RolesAllowed("user")
    @Path("/uploadUser")
    public Response setUploadUser(Map<String, String> params, @Context SecurityContext sc) {
        WebApiUser webApiUser = (WebApiUser) sc.getUserPrincipal();
        String password = params.get("password");
        String username = params.get("username");
        logger.info("New password is: " + password);
        if (UserRepository.setUploadUser(webApiUser, username, password)) {
            return Response.ok().build();
        }

        return Response.status(HttpStatus.SC_UNAUTHORIZED).build();
    }

    @GET
    @Path("/login/{type}")
    public Response login(@PathParam("type") String type, @Context HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append(request.getScheme()).append("://");
        sb.append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443)
            sb.append(":").append(request.getServerPort());
        sb.append(request.getRequestURI());
        sb.append("/oauth2callback");

        AuthUrlBuilder builder = AuthUrlBuilder.Builder(type);
        if (builder != null) {
            String state = CSRFTokenHandler.createToken(null);

            String authUrl = builder.withClientId(Constants.CLIENT_ID)
                    .withScope("openid email")
                    .withResponseType("code")
                    .withState(state)
                    .withRedirectUri(sb.toString()).build();

            return Response.seeOther(URI.create(authUrl)).build();
        }

        return Response.status(HttpStatus.SC_UNAUTHORIZED).build();
    }

    @GET
    @Path("/login/{type}/oauth2callback")
    @Produces(MediaType.TEXT_HTML)
    public Response loginCallback(@PathParam("type") String type,
                                  @QueryParam("state") String state,
                                  @QueryParam("code") String code,
                                  @QueryParam("authuser") Boolean authuser,
                                  @QueryParam("prompt") String prompt,
                                  @QueryParam("session_state") String sessionState,
                                  @Context HttpServletRequest request) {
        Object stateData = CSRFTokenHandler.getToken(state);
        if (stateData != null) {
            TokenRequestBuilder builder = TokenRequestBuilder.Builder(type);
            if (builder == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            String tokenRequestUrl = builder.withCode(code)
                    .withClientId(Constants.CLIENT_ID)
                    .withClientSecret(Constants.CLIENT_SECRET)
                    .withRedirectUri(request.getRequestURL().toString())
                    .withGrantType("authorization_code").build();

            try {
                URL url = new URL(builder.getBase());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream()));
                bufferedWriter.write(tokenRequestUrl);
                bufferedWriter.close();

                if (urlConnection.getResponseCode() < 300) {
                    TokenResponse tokenResponse = Utils.jacksonObjectMapper.readValue(urlConnection.getInputStream(), TokenResponse.class);
                    IdClaim idClaim = TokenVerifierFacade.verify(type, tokenResponse.getIdToken());
                    if (idClaim == null) return Response.status(Response.Status.UNAUTHORIZED).build();
                    // FIXME tokenResponse.g

                    de.konqi.fitapi.db.domain.User user = OAuthLoginRepository.createUser(idClaim.getIsssuer(), idClaim.getSubscriber(), idClaim.getEmail());
                    String sessionId = SessionRepository.createSession(user);
                    // Ref<de.konqi.fitapi.db.domain.User> userRef = OAuthLoginRepository.getLoginUser(idClaim.getIsssuer(), idClaim.getSubscriber());

                    int maxAge = 60 * 60 * 24 * 7;
                    // Cookie superkeks = new Cookie("superkeks", sessionId, "/web/api/", "localhost");
                    NewCookie newCookie = new NewCookie("session", sessionId, "/", null, null, maxAge, false);
                    LoginCallbackResponse loginCallbackResponse = new LoginCallbackResponse();
                    loginCallbackResponse.setSessionId(sessionId);
                    return Response.ok(new Viewable("/OAuth2ReturnServlet.jsp", loginCallbackResponse)).cookie(newCookie).build();
                }
            } catch (IOException e) {
                logger.warn("Unable to exchange code for id_token.", e);
            }
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();
    }


}
