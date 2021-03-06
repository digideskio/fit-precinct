package de.konqi.fitapi.rest.webapi.resource;

import com.google.appengine.api.blobstore.BlobKey;
import de.konqi.fitapi.Utils;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * Resource class for blob related functionality
 *
 * @author konqi
 */
@Path("/img")
@PermitAll
public class BlobResource {
    /**
     * Endpoint to serve a blob from Google's blob storage
     * @param blobKey
     * @param response
     * @return
     * @throws IOException
     */
    @GET
    @Path("/{id}")
    public Response getBlob(@PathParam("id") String blobKey, @Context HttpServletResponse response) throws IOException {
        Utils.blobstoreService.serve(new BlobKey(blobKey), response);
        return Response.ok().build();
    }
}
