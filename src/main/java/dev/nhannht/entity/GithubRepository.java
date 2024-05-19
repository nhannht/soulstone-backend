package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class GithubRepository {
    @Id
    @GeneratedValue
    @Column(name = "repo_id")
    Long repoId;

    @NonNull
    String owner ;
    @NonNull
    String repoName;

    @ManyToMany
    @JoinTable(name = "jointable_repo_topic",
            joinColumns = @JoinColumn(name = "repoJoinId"),
            inverseJoinColumns = @JoinColumn(name = "topicJoinId")
    )
    Set<Topic> topics = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "plugin_id")
    Plugin plugin = null;




//    @NonNull
//    @ManyToOne
//    @JoinColumn(name="id")
//    GithubUser author;


}
