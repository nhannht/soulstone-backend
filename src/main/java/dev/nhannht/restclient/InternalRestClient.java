package dev.nhannht.restclient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "/api")
public interface InternalRestClient {
    @GET
    @Path("/syncTopic")
    String callSyncTopic();

}
