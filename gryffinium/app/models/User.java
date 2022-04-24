package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Model;
import io.ebean.annotation.JsonIgnore;
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

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ProjectUser> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectUser> projects) {
        this.projects = projects;
    }
}
