package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class GithubRepository {
    @Id @GeneratedValue @Column(name="repo_id")
    Long repoId;

    @NonNull String owner;
    @NonNull String repoName;

    @ManyToMany
    Set<RepoTopic> topics;

    @OneToOne
    @JoinColumn(name="plugin_id",referencedColumnName = "plugin_id")
    ObsidianPlugin plugin;


//    @NonNull
//    @ManyToOne
//    @JoinColumn(name="id")
//    GithubUser author;


}
