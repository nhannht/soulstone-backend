package dev.nhannht.repository;

import dev.nhannht.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoTopicRepository extends JpaRepository<Topic,Long> {

}
