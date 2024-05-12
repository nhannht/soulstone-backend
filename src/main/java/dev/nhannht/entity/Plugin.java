package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.List;
import java.util.Set;

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
    Set<PluginVersion> pluginVersionList;

    @OneToOne(mappedBy = "plugin",cascade = CascadeType.ALL)
    GithubRepository repo;

    @CreationTimestamp
    private Instant createdOn;

    @UpdateTimestamp
    private Instant updatedOn;



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
