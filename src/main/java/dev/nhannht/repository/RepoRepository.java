package dev.nhannht.repository;

import dev.nhannht.entity.GithubRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepoRepository extends JpaRepository<GithubRepository, Long> {


}
