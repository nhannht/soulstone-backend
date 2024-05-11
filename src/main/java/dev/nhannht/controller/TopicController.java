package dev.nhannht.controller;

import dev.nhannht.DTO.TopicDto;
import dev.nhannht.repository.RepoTopicRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class TopicController {
    @Inject
    RepoTopicRepository repoTopicRepository;

    @Path("/topic/getall")
    @GET
    public Set<TopicDto> getAllTopics(){
        var topics = repoTopicRepository.findAll().stream().map(TopicDto::new).collect(Collectors.toSet());

        return  topics;
    }
}
