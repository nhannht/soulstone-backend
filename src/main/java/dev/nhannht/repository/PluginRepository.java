package dev.nhannht.repository;

import dev.nhannht.entity.Plugin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PluginRepository extends JpaRepository<Plugin,Long> {

    List<Plugin> findAll();
}