package dev.nhannht.repository;

import dev.nhannht.entity.GithubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubRepositoryRepository extends JpaRepository<GithubRepository,Long> {
}
