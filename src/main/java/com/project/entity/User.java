package com.project.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @Column(name = "username", unique = true, nullable = false)
    private String userName;

    @Column
    private String password;

    @Transient
    private String psw;

    @Column(name = "fullname")
    private String fullName;

    @NotEmpty
    @Column
    private String email;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String token;

    @NotEmpty
    @Column(length = 11)
    private String phone;

    @Column(name = "lastlogined", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastLogined;

    @Column(name = "is_enable", columnDefinition = "BIT")
    private Boolean isEnable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id")
            ,inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Wishlist> wishlists = new ArrayList<>();

    @Transient
    public void addRole(Role role){
        this.roles.add(role);
    }
}
