package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class PluginStatsDetails {
    @Id @GeneratedValue Long detailId;

    @OneToOne
    @JoinColumn(name="plugin_id",referencedColumnName = "plugin_id")
    ObsidianPlugin plugin;

    @NonNull Long downloads;

    @NonNull Long updated;

}
