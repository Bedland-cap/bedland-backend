package com.capgemini.bedland.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

import static org.hibernate.Length.LONG32;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "OWNER")
public class OwnerEntity extends AbstractEntity {

    @OneToMany(mappedBy = "flatOwnerEntity", cascade = CascadeType.ALL)
    private List<FlatEntity> flatEntities;

    @Column(nullable = false, length = 50, name = "LOGIN")
    private String login;

    @Column(nullable = false, length = 50, name = "PASSWORD")
    private String password;

    @Column(nullable = false, length = 50, name = "NAME")
    private String name;

    @Column(nullable = false, length = 50, name = "LAST_NAME")
    private String lastName;

    @Column(nullable = false, length = 50, name = "EMAIL")
    private String email;

    @Column(nullable = false, length = 25, name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AVATAR", length = LONG32)
    private byte[] avatar;

    @Column(unique = true, name ="TOKEN")
    private String token;

}
