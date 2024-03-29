package com.javamentor.jmp_project.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @Column(name = "id", columnDefinition = "bigserial")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login", unique = true, nullable = false, updatable = false)
    private String login;

    @Column(name = "password", nullable = false)
    private String password;

    //    @Enumerated(EnumType.STRING)
//    @Column(columnDefinition="enum('admin','user')")
    @Column(name = "role", nullable = false, columnDefinition = "varchar(31) default 'user'")
    private String role;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    public User() {
    }

    public User(String login, String password, String name, String email) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.email = email;
    }

    public User(String login, String password, String role, String name, String email) {
        this(login, password, name, email);
        this.role = role;
    }

    public User(Long id, String login, String password, String role, String name, String email) {
        this(login, password, name, email);
        this.id = id;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User {" +
                " id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                " }";
    }

}
