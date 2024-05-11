package dev.nhannht.DTO;

import dev.nhannht.entity.PluginStatsDetails;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.nhannht.entity.PluginStatsDetails}
 */
@Value
public class PluginStatsDetailsReducedDto implements Serializable {
    Long detailId;
    Long downloads;
    Long updated;

    public PluginStatsDetailsReducedDto(PluginStatsDetails statsDetails){
        this.detailId = statsDetails.getDetailId();
        this.downloads = statsDetails.getDownloads();
        this.updated = statsDetails.getUpdated();
    }
}