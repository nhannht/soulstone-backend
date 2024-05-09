package dev.nhannht.restclient;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(baseUri = "https://raw.githubusercontent.com/obsidianmd/obsidian-releases/master/")
public interface ObsidianPluginRestClient {

    @GET
    @Path("/community-plugin-stats.json")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    String getStatsOfPlugin() ;

    @GET
    @Path("/community-plugins.json")
    String getPlugins();


}
