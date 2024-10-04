package at.yehor.havryliuk.rest.proxy.demo.client;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient
public interface RemoteClient {

    //TODO Fix for non-baseUri: works for baseUri, does not for baseUri + /endpoint (status 301, Undefined error)
    @GET
    @Path("/{url}")
    Response getPage(@PathParam("url") String url);
}
