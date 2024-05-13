package dev.nhannht.repository;

import dev.nhannht.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PluginRepository extends JpaRepository<Plugin,Long> {
    Optional<Plugin> findByPluginIdIgnoreCase(String pluginId);

}
