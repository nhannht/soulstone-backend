package dev.nhannht.service;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApplicationScoped
public class SwitchManager {
    Boolean databaseUpdating = false;
}
