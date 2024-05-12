package dev.nhannht.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nhannht.entity.*;
import dev.nhannht.repository.*;
import dev.nhannht.restclient.GithubRestClient;
import dev.nhannht.restclient.InternalRestClient;
import dev.nhannht.restclient.ObsidianPluginRestClient;
import dev.nhannht.service.SwitchManager;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

@Path("/api")
public class DatabaseController {

    @Inject
    SwitchManager switchManager;


    @RestClient
    ObsidianPluginRestClient obsidianPluginRestClient;

    @Inject
    PluginRepository pluginRepository;

    @Inject
    PluginStatsDetailsRepository pluginStatsDetailsRepository;

    @Inject
    PluginVersionRepository pluginVersionRepository;


    private void initDatabaseInternal() throws JsonProcessingException {
        switchManager.setDatabaseUpdating(true);
        ObjectMapper mapper = new ObjectMapper();
        var pluginsList = mapper.readTree(obsidianPluginRestClient.getPlugins());
        var pluginStatsList = mapper.readTree(obsidianPluginRestClient.getStatsOfPlugin());


        StreamSupport.stream(
                        Spliterators.spliteratorUnknownSize(
                                pluginsList.elements(),
                                Spliterator.ORDERED
                        ), false)
//                .limit(10)
                .forEach(p -> {
                    var id = p.get("id").textValue();
                    var pluginStats = pluginStatsList.get(id);
                    var versions = new HashSet<PluginVersion>();

                    var name = p.get("name").textValue();
                    var author = p.get("author").textValue();
                    var description = p.get("description").textValue();
                    var repoFullName = p.get("repo").textValue();


                    var downloads = pluginStats.get("downloads").longValue();
                    var updated = pluginStats.get("updated").longValue();

                    var plugin = new Plugin(id, name, author, description);
                    var pluginDetail = new PluginStatsDetails(downloads, updated);
                    pluginDetail.setPlugin(plugin);

                    pluginRepository.save(plugin);
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

                    JsonNode repoJsonNode;
                    try {
                        var repoOwner = repoFullName.split("/")[0];
                        var repoName = repoFullName.split("/")[1];
                        repoJsonNode = githubRestClient.getRepo(token, repoOwner, repoName);
                        var repo = new GithubRepository(repoOwner, repoName);

                        repo.setPlugin(plugin);
                        var topicJsonNode = repoJsonNode.get("topics");
                        var topicsList = new HashSet<Topic>();
                        repoRepository.save(repo);


                        topicJsonNode.elements().forEachRemaining(e -> {
                            var topic = new Topic(e.textValue());
                            topicsList.add(topic);

                        });
                        repo.setTopics(topicsList);

                        topicRepository.saveAll(topicsList);
                        repoRepository.save(repo);


                    } catch (ClientWebApplicationException e) {
                        System.out.println(e);
                        // do nothing
                    }

//
//
                })
        ;
        switchManager.setDatabaseUpdating(false);
    }

    @GET
    @RolesAllowed("admin")
    @Path("/syncTopic")
    public RestResponse<Object> syncTopic() {
        if (switchManager.getDatabaseUpdating()) {

            return RestResponse
                    .ResponseBuilder
                    .create(400)
                    .entity("Database is blocking, currently updating in background")
                    .build();
        }


        var repos = repoRepository.findAll();
        var tempHashMap = new HashMap<Topic, HashSet<GithubRepository>>();
        repos.forEach(r -> {
            var topics = r.getTopics();
            topics.forEach(t -> {
                var repoOfTopic = Optional
                        .ofNullable(tempHashMap.get(t))
                        .orElse(new HashSet<>());
                repoOfTopic.add(r);
                tempHashMap.put(t, repoOfTopic);
            });
        });
        tempHashMap.forEach((topic, reposOfTopic) -> {
            topic.setRepos(reposOfTopic);
            topicRepository.save(topic);
        });

        return RestResponse
                .ResponseBuilder
                .create(200)
                .entity("Successful sync topic")
                .build();


    }

    @Inject
    ManagedExecutor managedExecutor;

    @RestClient
    InternalRestClient internalRestClient;


    @GET
    @RolesAllowed("admin")
    @Path("/initDatabase")
    public RestResponse<?> initDataBase() throws JsonProcessingException, ExecutionException, InterruptedException {


        managedExecutor.submit(() -> {
            try {
                initDatabaseInternal();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        });


        return RestResponse.ResponseBuilder
                .create(200)
                .entity("Start init db in the background, it may took time and will blocking the database.")
                .build();

    }

    @RestClient
    GithubRestClient githubRestClient;

    @ConfigProperty(name = "github_token")
    String token;


    @Inject
    RepoRepository repoRepository;

    @Inject
    TopicRepository topicRepository;



    @GET
    @Path("/resetDb")
    @RolesAllowed("admin")
    public String resetDb() {
        topicRepository.deleteAll();
        repoRepository.deleteAll();
        pluginRepository.deleteAll();
        pluginVersionRepository.deleteAll();
        return "success";
    }


}
