package dev.nhannht.DTO;

import dev.nhannht.entity.PluginVersion;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.nhannht.entity.PluginVersion}
 */
@Value
public class PluginVersionReducedDto implements Serializable {
    Long versionId;
    String versionName;
    Long downloadNumbers;

    public PluginVersionReducedDto(PluginVersion pluginVersion){
        this.versionId = pluginVersion.getVersionId();
        this.versionName = pluginVersion.getVersionName();
        this.downloadNumbers = pluginVersion.getDownloadNumbers();
    }
}