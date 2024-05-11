package dev.nhannht.DTO;

import dev.nhannht.entity.Plugin;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.nhannht.entity.Plugin}
 */
@Value
public class PluginReducedDto implements Serializable {
    String pluginId;
    String name;
    String author;
    String description;

    public PluginReducedDto(Plugin plugin){
        this.pluginId = plugin.getPluginId();
        this.name = plugin.getName();
        this.author = plugin.getAuthor();
        this.description = plugin.getDescription();
    }
}