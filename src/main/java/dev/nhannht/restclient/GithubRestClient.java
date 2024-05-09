package dev.nhannht.restclient;

import com.fasterxml.jackson.databind.JsonNode;
import io.quarkus.rest.client.reactive.NotBody;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://api.github.com/")
public interface GithubRestClient {

    @GET
    @Path("/repos/{user}/{repo}/contents/{file}")
    @ClientHeaderParam(name = "Authorization", value = "Bearer {token}")
     JsonNode getContent(@NotBody  String token,
                         @PathParam("user") String user,
                         @PathParam("repo") String repo,
                         @PathParam("file")  String file
    );

    @GET
    @Path("/repos/{owner}/{repo}")
    @ClientHeaderParam(name = "Authorization",value = "Bearer {token}")
    JsonNode getRepo(@NotBody String token,
                     @PathParam("owner") String owner,
                     @PathParam("repo") String repo) ;
}
