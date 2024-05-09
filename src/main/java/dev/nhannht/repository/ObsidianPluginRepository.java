package dev.nhannht.repository;

import dev.nhannht.entity.ObsidianPlugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObsidianPluginRepository extends JpaRepository<ObsidianPlugin,Long> {

    List<ObsidianPlugin> findAll();
}
