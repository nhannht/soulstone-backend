package dev.nhannht.DTO;

import dev.nhannht.entity.Topic;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * DTO for {@link Topic}
 */
@Value
public class TopicDto implements Serializable {
    String name;
    Set<GithubRepositoryReducedDto> repos;
    Instant createdOn;
    Instant updatedOn;


    public TopicDto(Topic topic){
        this.name = topic.getName();
        this.repos = topic
                .getRepos()
                .stream()
                .map(GithubRepositoryReducedDto::new)
                .collect(Collectors.toSet());
        this.createdOn = topic.getCreatedOn();
        this.updatedOn = topic.getUpdatedOn();
    }

}