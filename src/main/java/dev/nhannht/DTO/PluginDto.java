package dev.nhannht.DTO;

import dev.nhannht.entity.Plugin;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link dev.nhannht.entity.Plugin}
 */
@Value
public class PluginDto implements Serializable {
    String pluginId;
    String name;
    String author;
    String description;
    PluginStatsDetailsReducedDto statsDetails;
    Set<PluginVersionReducedDto> pluginVersionList;
    GithubRepositoryReducedDto repo;
    Instant createdOn;
    Instant updatedOn;


    public PluginDto(Plugin plugin){
        this.pluginId = plugin.getPluginId();
        this.name = plugin.getName();
        this.author = plugin.getAuthor();
        this.description = plugin.getDescription();
        this.statsDetails = new PluginStatsDetailsReducedDto(plugin.getStatsDetails());
        this.pluginVersionList = plugin.getPluginVersionList().stream().map(PluginVersionReducedDto::new).collect(Collectors.toSet());
        this.repo = new GithubRepositoryReducedDto(plugin.getRepo());
        this.createdOn = plugin.getCreatedOn();
        this.updatedOn = plugin.getUpdatedOn();

    }
}