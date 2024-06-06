package com.example.groupproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //AUTO)
    private int user_id;

    @Column(nullable = false)
    private String cust_name;

    @Column(nullable = false,unique = true)
    private String cust_email;

    @Column(nullable = false)
    private String cust_password;

    @Column(nullable = false)
    private String cust_phonenum;

    @Column(nullable = false)
    private String address;

    @ManyToMany(fetch = FetchType.EAGER ,cascade=CascadeType.ALL)
    @JoinTable(name = "users_role",
            joinColumns = {@JoinColumn(name="user_id", referencedColumnName = "id")},
            inverseJoinColumns ={ @JoinColumn(name = "role_id", referencedColumnName = "id")})


    private List<Role> roles = new ArrayList<>();
}




