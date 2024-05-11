package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class Topic {

    @NonNull
    @Column(name = "topic_name")
    @Id
    String name;

    @ManyToMany
    @JoinTable(name = "jointable_repo_topic",
            joinColumns = @JoinColumn(name = "topicJoinId"),
            inverseJoinColumns = @JoinColumn(name = "repoJoinId")
    )

    Set<GithubRepository> repos;
}
