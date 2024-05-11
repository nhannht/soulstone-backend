package dev.nhannht;

import dev.nhannht.security.User;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Singleton
public class HelloWorld {
    @ConfigProperty(name = "admin_username") String username;
    @ConfigProperty(name = "admin_password") String password;

    @Startup
    @Transactional
    public void helloWorld(){
        User.deleteAll();
        User.add(username,password,"admin");
    }

}
