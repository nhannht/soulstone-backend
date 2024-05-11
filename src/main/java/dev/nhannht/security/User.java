package dev.nhannht.security;

import io.quarkus.arc.All;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.*;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import lombok.*;

@Entity
@UserDefinition
@Getter
@Setter
@Table(name="test_user")
public class User extends PanacheEntity {
    @Username
    public String username;
    @Password(PasswordType.CLEAR)
    public String password;
    @Roles
    public String role;

    @Transactional
    public static void add(String username, String password, String role) {
        User user = new User();
        user.username = username;
        user.password = password;
        user.role = role;
        user.persist();
    }
}
