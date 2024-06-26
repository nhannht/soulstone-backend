package dev.nhannht.DTO;

import dev.nhannht.entity.GithubRepository;
import dev.nhannht.entity.Plugin;
import lombok.Value;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link dev.nhannht.entity.GithubRepository}
 */
@Value
public class GithubRepositoryDto implements Serializable {
    Long repoId;
    String owner;
    String repoName;
    Set<TopicReducedDto> topics;
    PluginReducedDto plugin;

    public GithubRepositoryDto(GithubRepository repo) {
        if (repo == null) {
            this.repoId = (long) -1;
            this.owner = "null";
            this.repoName = "null";
            this.topics = new HashSet<>();
            this.plugin = new PluginReducedDto(new Plugin());
        } else {
            this.repoId = repo.getRepoId();
            this.owner = repo.getOwner();
            this.repoName = repo.getRepoName();
            this.topics = repo.getTopics().stream().map(TopicReducedDto::new).collect(Collectors.toSet());
            this.plugin = new PluginReducedDto(repo.getPlugin());

        }


    }

}