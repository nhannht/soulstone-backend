package dev.nhannht.service;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@Getter
@Setter
@ApplicationScoped
public class MultiThreadingService {
    private ExecutorService executorService;

    @PostConstruct
    void init(){
        this.executorService = Executors.newSingleThreadExecutor();
    }

}
