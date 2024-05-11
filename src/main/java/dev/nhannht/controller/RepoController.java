package dev.nhannht.controller;

import dev.nhannht.DTO.GithubRepositoryDto;
import dev.nhannht.repository.RepoRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class RepoController {
    @Inject
    RepoRepository repoRepository;

    @GET
    @Path("/repo/all")
    public Set<GithubRepositoryDto> getAllRepo(){
        return repoRepository
                .findAll()
                .stream()
                .map(GithubRepositoryDto::new)
                .collect(Collectors.toSet());
    }

    @GET
    @Path("/repo/get")
    public RestResponse<?> findByFullName(
            @QueryParam("owner") String owner,
            @QueryParam("repo") String repo
    ){
        var r = repoRepository.findByOwnerIgnoreCaseAndRepoNameIgnoreCase(owner,repo);
        if (r.isPresent()){
            return RestResponse.ResponseBuilder
                    .ok(new GithubRepositoryDto(r.get()))
                    .build();
        } else {
            return RestResponse.ResponseBuilder
                    .notFound()
                    .entity("not found")
                    .build();
        }
    }
}
