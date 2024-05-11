package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Plugin {
    @Id @Column(name="plugin_id") @NonNull
    String pluginId;

    @NonNull
    String name;


    @NonNull
    String author;

    @NonNull
    @Column(length = 500)
    String description;


    @OneToOne(mappedBy = "plugin",cascade = CascadeType.ALL)
    PluginStatsDetails statsDetails;

    @OneToMany(mappedBy = "plugin",cascade = CascadeType.ALL)
    List<PluginVersion> pluginVersionList;

    @OneToOne(mappedBy = "plugin",cascade = CascadeType.ALL)
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
