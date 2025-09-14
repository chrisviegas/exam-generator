package dev.viegas.examgenerator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
public class ApplicationUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "The field username cannot be empty")
    @Column(unique = true)
    private String username;
    @NotBlank(message = "The field password cannot be empty")
    private String password;
    @OneToOne
    private Professor professor;

    public ApplicationUser() {
    }

    public ApplicationUser(Long id, String username, String password, Professor professor) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.professor = professor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
