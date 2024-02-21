package com.example.immo.models;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
/*
 * @Data est une annotation Lombok. Nul besoin d’ajouter les getters et les
 * setters. La librairie Lombok s’en charge pour nous. Très utile pour alléger
 * le code.
 */
@Entity
/*
 * @Entity est une annotation qui indique que la classe correspond à une table
 * de la base de données.
 */
// @Builder // so we can build a new user in our tests
@AllArgsConstructor
@Builder
@Table(name = "utilisateurs")
/* spring security needs some UserDetails methods to be implemented */
public class User implements UserDetails {

    @Id
    /* GeneratedValue / Identity : autoincrement a number when id is missing */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstname;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastname;

    @Column(name = "email", unique = true) // !!! unique
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles_junction", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
            @JoinColumn(name = "role_id") })
    private Set<Role> authorities;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date creation;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date update;

    // !!! https://www.baeldung.com/jpa-no-argument-constructor-entity-class
    public User() {
        super();
        authorities = new HashSet<>();
    }

    /*
     * public User(Long userId, String firstName, String lastName, String email,
     * String password,
     * Set<Role> authorities, Date creation, Date update) {
     * super();
     * this.userId = userId;
     * this.firstname = firstName;
     * this.lastname = lastName;
     * this.email = email;
     * this.password = password;
     * this.authorities = authorities;
     * this.creation = creation;
     * this.update = update;
     * }
     */

    public User(Long userId, String firstname, String lastname, String email, String password,
            Set<Role> authorities) {
        super();
        this.userId = userId;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
        this.creation = new Date();
        this.update = new Date();
    }

    /*
     * @Enumerated(EnumType.STRING)
     * private Role role;
     */

    // methods that needs to be overriden to implements userDetails

    /*
     * @Override
     * public Collection<? extends GrantedAuthority> getAuthorities() {
     * String userRole = role.name();
     * if (userRole.contains("ROLE_") == false) {
     * userRole = "ROLE_" + userRole;
     * }
     * return List.of(new SimpleGrantedAuthority(userRole));
     * }
     */

    public Long getUserId() {
        return this.userId;
    }

    public void setId(Long userId) {
        this.userId = userId;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void setUsername(String username) {
        this.email = username;
    }

    /*
     * If you want account locking capabilities create variables and ways to set
     * them for the methods below
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}