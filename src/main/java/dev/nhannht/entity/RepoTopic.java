package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class RepoTopic {


    @Id @NonNull @Column(name="topic_name")
    String name;

    @ManyToMany
    Set<GithubRepository> repos;
}
