package dev.nhannht.controller;

import dev.nhannht.DTO.PluginDto;
import dev.nhannht.repository.PluginRepository;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import org.jboss.resteasy.reactive.RestResponse;

import java.util.Set;
import java.util.stream.Collectors;

@Path("/api")
public class PluginController {
    @Inject
    PluginRepository pluginRepository;

    @Path("/plugin/all")
    @GET
    public Set<PluginDto> getAllPlugin(){
        return pluginRepository.findAll().stream().map(PluginDto::new).collect(Collectors.toSet());


    }

    @Path("/plugin/{id}")
    @GET
    public RestResponse<?> getPluginById(@PathParam("id") Long id){
        var result =  pluginRepository
                .findById(id)
                ;

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
