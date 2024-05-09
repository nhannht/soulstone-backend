package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class ObsidianPlugin {
    @Id @Column(name="plugin_id") @NonNull
    String pluginId;

    @NonNull
    String name;


    @NonNull
    String author;

    @NonNull
    @Column(length = 500)
    String description;


    @OneToOne(mappedBy = "plugin")
    PluginStatsDetails statsDetails;

    @OneToMany(mappedBy = "plugin",cascade = CascadeType.ALL)
    List<PluginVersion> pluginVersionList;

    @OneToOne(mappedBy = "plugin",cascade = CascadeType.MERGE)
    GithubRepository repo;



//    @NonNull
//    @OneToOne
//    GithubRepository repo;
//
//
//    @NonNull
//    @ManyToOne
//    @JoinColumn(name="id")
//    GithubUser user;


}
