package dev.nhannht.controller;

import dev.nhannht.DTO.GithubRepositoryDto;
import dev.nhannht.repository.RepoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class RepoController {
    @Inject
    RepoRepository repoRepository;

    @GET
    @Path("/repo/all")
    public Set<GithubRepositoryDto> getAllRepo(){
        return repoRepository.findAll().stream().map(GithubRepositoryDto::new).collect(Collectors.toSet());
    }
}
