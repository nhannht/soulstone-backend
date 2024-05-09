package dev.nhannht.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class GithubUser {
    @Id
    @GeneratedValue
    Long userId;

    @NonNull String userName;

//    @NonNull @OneToMany(mappedBy = "githubUser")
//    Set<ObsidianPlugin> plugins;
//
//    @NonNull
//    @OneToMany(mappedBy = "author")
//    Set<GithubRepository> repos;

}
