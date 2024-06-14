package dev.nhannht.controller;

import dev.nhannht.DTO.PluginDto;
import dev.nhannht.repository.PluginRepository;
import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import org.jboss.resteasy.reactive.RestResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class PluginController {
    @Inject
    PluginRepository pluginRepository;

    @Path("/plugin/all")
    @GET
    @CacheResult(cacheName="getAllPlugin-cache")
    public Set<PluginDto> getAllPlugin(@QueryParam("page") Integer page){
//        System.out.println(pluginRepository.findAll());
        return pluginRepository.findAll(PageRequest.of(page,10)).stream().map(PluginDto::new).collect(Collectors.toSet());


    }

    @CacheResult(cacheName = "getPluginById-cache")
    @Path("/plugin/{id}")
    @GET
    public RestResponse<?> getPluginById(@PathParam("id") String pluginId){
        var result =  pluginRepository
                .findByPluginIdIgnoreCase(pluginId);

        if (result.isPresent()){
            return RestResponse
                    .ResponseBuilder
                    .ok(new PluginDto(result.get()))
                    .build();

        } else {
            return RestResponse.ResponseBuilder
                    .create(404)
                    .entity("Not found")
                    .build();
        }





    }
}
