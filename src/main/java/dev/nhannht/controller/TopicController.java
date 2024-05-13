package dev.nhannht.controller;

import dev.nhannht.DTO.TopicDto;
import dev.nhannht.repository.TopicRepository;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class TopicController {
    @Inject
    TopicRepository topicRepository;

    @CacheResult(cacheName = "getAllTopic-cache")
    @Path("/topic/getall")
    @GET
    public Set<TopicDto> getAllTopics(){
        var topics = topicRepository.findAll().stream().map(TopicDto::new).collect(Collectors.toSet());

        return  topics;
    }

    @CacheResult(cacheName = "getTopicByName-cache")
    @Path("/topic/{name}")
    @GET
    public RestResponse<?> getTopicByName(@PathParam("name") String name){
        var  topic = topicRepository.findById(name);
        if (topic.isPresent()){
            return RestResponse
                    .ResponseBuilder
                    .ok(new TopicDto(topic.get()))
                    .build();
        } else {
            return RestResponse.ResponseBuilder
                    .create(404)
                    .entity("Not found")
                    .build();
        }

    }
}
