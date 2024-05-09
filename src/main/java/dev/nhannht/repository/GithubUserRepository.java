package dev.nhannht.repository;

import dev.nhannht.entity.GithubUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubUserRepository extends JpaRepository<GithubUser,Long> {
}
