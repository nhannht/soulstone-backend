package dev.nhannht.repository;

import dev.nhannht.entity.Plugin;
import dev.nhannht.entity.PluginVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PluginVersionRepository extends JpaRepository<PluginVersion,Long> {


    Optional<PluginVersion> findByVersionNameIgnoreCase(String versionName);

    Optional<PluginVersion> findByVersionNameIgnoreCaseAndPlugin_PluginIdIgnoreCase(String versionName, String pluginId);


}
