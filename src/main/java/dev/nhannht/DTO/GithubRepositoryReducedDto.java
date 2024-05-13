package dev.nhannht.DTO;

import dev.nhannht.entity.GithubRepository;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.nhannht.entity.GithubRepository}
 */
@Value
public class GithubRepositoryReducedDto implements Serializable {
    Long repoId;
    String owner;
    String repoName;

    public GithubRepositoryReducedDto(GithubRepository repo) {
        if (repo == null) {
            this.repoId = (long) -1;
            this.owner = "null";
            this.repoName = "null";

        } else {
            this.repoId = repo.getRepoId();
            this.owner = repo.getOwner();
            this.repoName = repo.getRepoName();
        }
    }
}