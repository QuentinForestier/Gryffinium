package models;

import io.ebean.Model;
import io.ebean.annotation.NotNull;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User extends Model {
    @Id
    public UUID id;

    @NotNull
    public String name;

    @NotNull
    public String email;

    @NotNull
    public String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<ProjectUser> projects;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }
}
