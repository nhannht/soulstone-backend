package dev.nhannht.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.nhannht.entity.*;
import dev.nhannht.repository.*;
import dev.nhannht.restclient.GithubRestClient;
import dev.nhannht.restclient.ObsidianPluginRestClient;
import dev.nhannht.service.MultiThreadingService;
import dev.nhannht.service.SwitchManager;
import io.quarkus.scheduler.Scheduled;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Path("/api")
public class DatabaseController {

    @Inject
    SwitchManager switchManager;

    final AtomicBoolean isUpdated = new AtomicBoolean(false);


    @RestClient
    ObsidianPluginRestClient obsidianPluginRestClient;

    @Inject
    PluginRepository pluginRepository;

    @Inject
    PluginStatsDetailsRepository pluginStatsDetailsRepository;

    @Inject
    PluginVersionRepository pluginVersionRepository;


    @Transactional
    void initDatabaseInternal() throws JsonProcessingException {
        switchManager.setDatabaseUpdating(Boolean.TRUE);
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
                updatePlugin(pluginStatsList, p, id);

//
//
            })
        ;
        switchManager.setDatabaseUpdating(Boolean.FALSE);
    }

    void updatePlugin(JsonNode pluginStatsList, JsonNode p, String id) {
        System.out.println("In updated plugin function");
        var pluginStats = pluginStatsList.get(id);
        var versions = new HashSet<PluginVersion>();

        var name = p.get("name").textValue();
        var author = p.get("author").textValue();
        var description = p.get("description").textValue();
        var repoFullName = p.get("repo").textValue();

        var downloads = Long.valueOf(pluginStats.get("downloads").longValue());
        var updated = Long.valueOf(pluginStats.get("updated").longValue());

        var plugin = new Plugin(id, name, author, description);
        pluginRepository.save(plugin);

        var tempPlugin = pluginRepository.findByPluginIdIgnoreCase(id);

        System.out.println("Sucessfull save plugin without details");
        var pluginDetail = new PluginStatsDetails(downloads, updated);
        tempPlugin.ifPresent(pluginDetail::setPlugin);

        System.out.println("Just set plugin details for plugin ");
        pluginStatsDetailsRepository.save(pluginDetail);

        pluginStats.fields().forEachRemaining(field -> {
            var versionName = field.getKey();
            var downloadsPerVer = Long.valueOf(field.getValue().longValue());
            if (!Objects.equals(versionName, "downloads") && !Objects.equals(versionName, "updated")) {
                var version = new PluginVersion(versionName, downloadsPerVer);
                versions.add(version);
            }
        });

        versions.forEach(ver -> ver.setPlugin(plugin));
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
            throw new RuntimeException(e);
            // do nothing
        }
    }


    @Inject
    ManagedExecutor managedExecutor;

    @Inject
    MultiThreadingService multiThreadingService;


    @Transactional
    void updateDbInternal() throws JsonProcessingException {
//        System.out.println("Is updating db internal");
        switchManager.setDatabaseUpdating(Boolean.TRUE);
        var mapper = new ObjectMapper();

        var pluginsList = mapper.readTree(obsidianPluginRestClient.getPlugins());
        var pluginStatsList = mapper.readTree(obsidianPluginRestClient.getStatsOfPlugin());
        StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(
                    pluginsList.elements(),
                    Spliterator.ORDERED
                ), false)
//            .limit(11)
            .forEach(p -> {
//            System.out.println("in stream");
                var pluginId = p.get("id").textValue();

                var pluginExistInDbQ = pluginRepository.findByPluginIdIgnoreCase(pluginId);
//            System.out.println("1");
                pluginExistInDbQ.ifPresent(plugin -> System.out.println(plugin.getName()));
                if (pluginExistInDbQ.isEmpty()) {
                    System.out.println("This plugin is not exist " + pluginId);
                    updatePlugin(pluginStatsList, p, pluginId);

                } else {
                    var pluginFromDb = pluginExistInDbQ.get();
                    pluginFromDb.setName(p.get("name").textValue());
                    pluginFromDb.setAuthor(p.get("author").textValue());
                    pluginFromDb.setDescription(p.get("description").textValue());

                    pluginFromDb.setUpdatedOn(Instant.now());
                    pluginRepository.save(pluginFromDb);
                    var pluginStats = pluginStatsList.get(pluginId);
                    var totalDownloads = Long.valueOf(pluginStats.get("downloads").longValue());
                    var updated = Long.valueOf(pluginStats.get("updated").longValue());
                    var details = pluginFromDb.getStatsDetails();
                    details.setUpdated(updated);
                    details.setDownloads(totalDownloads);
                    pluginFromDb.setStatsDetails(details);
                    pluginRepository.save(pluginFromDb);

                    pluginStats.fields().forEachRemaining(field -> {
                        var versionName = field.getKey();
                        var downloadsPerVer = Long.valueOf(field.getValue().longValue());
                        var versionWithNameFromDatabaseExists = pluginVersionRepository
                            .findByVersionNameIgnoreCaseAndPlugin_PluginIdIgnoreCase(versionName, pluginFromDb.getPluginId());
                        if (versionWithNameFromDatabaseExists.isEmpty()) {
                            var newVersion = new PluginVersion(versionName, downloadsPerVer);
                            newVersion.setPlugin(pluginFromDb);
                            pluginVersionRepository.save(newVersion);
                        }

                    });

                    // update repo
                    var repoFullName = p.get("repo").textValue().split("/");
                    var repoOwner = repoFullName[0];
                    var repoName = repoFullName[1];
                    var repoInDbExistQ = repoRepository.findByOwnerIgnoreCaseAndRepoNameIgnoreCase(repoOwner, repoName);
                    var repoFromRemote = githubRestClient.getRepo(token, repoOwner, repoName);
                    if (repoInDbExistQ.isEmpty()) {
                        var newRepo = new GithubRepository(repoOwner, repoName);
                        repoRepository.save(newRepo);

                        updateTopicInEachRemote(repoFromRemote, newRepo);

                    } else {
                        var repo = repoInDbExistQ.get();
                        updateTopicInEachRemote(repoFromRemote, repo);

                    }

                }
            });
//        System.out.println("Finish");
        switchManager.setDatabaseUpdating(Boolean.FALSE);
//        System.out.println(switchManager.getDatabaseUpdating());

    }

    @Scheduled(every = "3600s")
    void updatedDatabaseSchedule() {
        if (switchManager.getDatabaseUpdating()) {
            return;
        }
        multiThreadingService.getExecutorService().submit(() -> {
            try {
                updateDbInternal();
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        });

    }


    @GET
    @RolesAllowed("admin")
    @Path("/updateDb")
    public RestResponse<?> updateDataBase() throws JsonProcessingException {
//        if (switchManager.getDatabaseUpdating()) {
//            return RestResponse
//                .ResponseBuilder
//                .create(503)
//                .entity("Database is blocking in the background, maybe it is updating")
//                .build();
//        }
//        multiThreadingService.getExecutorService().submit(() -> {
//            try {
                updateDbInternal();
//            } catch (JsonProcessingException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        updateDbInternal();
//        return RestResponse.ResponseBuilder
//            .create(200)
//            .entity("Start init db in the background, it may took time and will blocking the database.")
//            .build();

        return RestResponse.ResponseBuilder.create(200).build();


    }

    private void updateTopicInEachRemote(JsonNode repoFromRemote, GithubRepository newRepo) {
        var topicJsonNode = repoFromRemote.get("topics");
        topicJsonNode.elements().forEachRemaining(topic -> {
            var topicExistInDbQ = topicRepository.findById(topic.textValue());
            if (topicExistInDbQ.isEmpty()) {
                var newTopic = new Topic(topic.textValue());
                topicRepository.save(newTopic);
                newRepo.getTopics().add(newTopic);

            }
        });
        repoRepository.save(newRepo);
    }


    @GET
    @RolesAllowed("admin")
    @Path("/initDatabase")
    public RestResponse<?> initDataBase() {
        if (switchManager.getDatabaseUpdating()) {
            return RestResponse.ResponseBuilder
                .create(503)
                .entity("Database is busay and locked")
                .build();
        }

        multiThreadingService.getExecutorService().submit(() -> {
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
