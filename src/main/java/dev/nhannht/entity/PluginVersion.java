package dev.nhannht.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
public class PluginVersion {

    @Id @GeneratedValue @Column(name="version_id")
    Long versionId;

    @NonNull @Column(name="version_name")
    String versionName;

    @NonNull @Column(name="downloads_count")
    Long downloadNumbers;

    @ManyToOne
    @JoinColumn(name="plugin_id",referencedColumnName = "plugin_id")
    Plugin plugin;


}
