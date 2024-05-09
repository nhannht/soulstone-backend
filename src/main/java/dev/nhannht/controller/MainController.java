package dev.nhannht.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nhannht.entity.*;
import dev.nhannht.repository.*;
import dev.nhannht.restclient.GithubRestClient;
import dev.nhannht.restclient.ObsidianPluginRestClient;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.StreamSupport;

@Path("/api")
public class MainController {


    @RestClient
    ObsidianPluginRestClient obsidianPluginRestClient;

    @Inject
    ObsidianPluginRepository obsidianPluginRepository;

    @Inject
    PluginStatsDetailsRepository pluginStatsDetailsRepository;

    @Inject
    PluginVersionRepository pluginVersionRepository;


    @GET
    @Path("/stats")
    public Iterator<String> getStats() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode realObject = mapper.readTree(obsidianPluginRestClient.getStatsOfPlugin());
        return realObject.fieldNames();
    }

    @GET
    @Path("/plugins")
    public List<ObsidianPlugin> gePlugins() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        var pluginsList = mapper.readTree(obsidianPluginRestClient.getPlugins());
        var pluginStatsList = mapper.readTree(obsidianPluginRestClient.getStatsOfPlugin());

        StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                                pluginsList.elements(),
                                Spliterator.ORDERED
                        ), false)
                .limit(10)
                .forEach(p -> {


                    var id = p.get("id").textValue();
                    var pluginStats = pluginStatsList.get(id);
                    var versions = new ArrayList<PluginVersion>();

                    var name = p.get("name").textValue();
                    var author = p.get("author").textValue();
                    var description = p.get("description").textValue();
                    var repoFullName = p.get("repo").textValue();


                    var downloads = pluginStats.get("downloads").longValue();
                    var updated = pluginStats.get("updated").longValue();

                    var plugin = new ObsidianPlugin(id, name, author, description);
                    obsidianPluginRepository.save(plugin);
                    var pluginDetail = new PluginStatsDetails(downloads, updated);
                    pluginDetail.setPlugin(plugin);
                    pluginStatsDetailsRepository.save(pluginDetail);

                    pluginStats.fields().forEachRemaining(field -> {
                        var versionName = field.getKey();
                        var downloadsPerVer = field.getValue().longValue();
                        if (!Objects.equals(versionName, "downloads") && !Objects.equals(versionName, "updated")) {
                            var version = new PluginVersion(versionName, downloadsPerVer);
                            versions.add(version);
                        }
                    });

                    versions.forEach(ver -> {
                        ver.setPlugin(plugin);
                    });
                    pluginVersionRepository.saveAll(versions);

//
//                    JsonNode repoJsonNode;
//                    try {
//                        repoJsonNode = githubRestClient.getRepo(token, repoOwner, repoName);
//                        var repo = new GithubRepository(repoOwner, repoName);
//
//                        repo.setPlugin(plugin);
//                        var topicJsonNode = repoJsonNode.get("topics");
//                        var topicsList = new HashSet<RepoTopic>();
//                        topicJsonNode.elements().forEachRemaining(e -> {
//                            topicsList.add(new RepoTopic(e.textValue()));
//
//                        });
//                        repo.setTopics(topicsList);
//
//                        repoTopicRepository.saveAll(topicsList);
//                        githubRepositoryRepository.save(repo);
//
//
//                    } catch (ClientWebApplicationException e) {
//                        // do nothing
//                    }
//

//



                });

        return obsidianPluginRepository.findAll();

    }

    @RestClient
    GithubRestClient githubRestClient;

    @ConfigProperty(name = "github_token")
    String token;

    @GET
    @Path("/content")
    public String env() {
        var jsonNode = githubRestClient.getContent(token, "obsidianmd", "obsidian-releases", "README.md");
        var decodedBytes = Base64.getMimeDecoder().decode(jsonNode.get("content").textValue());
        return new String(decodedBytes, StandardCharsets.UTF_8);

    }

    @Inject
    GithubRepositoryRepository githubRepositoryRepository;

    @Inject
    RepoTopicRepository repoTopicRepository;

    @GET
    @Path("/repo")
    public JsonNode repo() {
        return githubRestClient.getRepo(token, "argenos", "nldates-obsidian");
    }

    @GET
    @Path("/resetDb")
    public String resetDb(){
        obsidianPluginRepository.deleteAll();
        pluginVersionRepository.deleteAll();
        pluginVersionRepository.deleteAll();
        repoTopicRepository.deleteAll();
        githubRepositoryRepository.deleteAll();
        return "success";
    }


}
