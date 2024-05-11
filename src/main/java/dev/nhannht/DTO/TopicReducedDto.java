package dev.nhannht.DTO;

import dev.nhannht.entity.Topic;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link dev.nhannht.entity.Topic}
 */
@Value
public class TopicReducedDto implements Serializable {
    String name;

    public TopicReducedDto(Topic topic){
        this.name = topic.getName();

    }
}