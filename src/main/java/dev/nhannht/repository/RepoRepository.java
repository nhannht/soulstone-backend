package dev.nhannht.repository;

import dev.nhannht.entity.GithubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RepoRepository extends JpaRepository<GithubRepository, Long> {
    Optional<GithubRepository> findByOwnerIgnoreCaseAndRepoNameIgnoreCase(String owner, String repoName);


}
