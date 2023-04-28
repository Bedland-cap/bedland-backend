package com.capgemini.bedland.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "ADMIN")
public class AdminEntity extends AbstractEntity {

    @Column(nullable = false, length = 50, name = "LOGIN")
    private String login;

    @Column(nullable = false, length = 50, name = "PASSWORD")
    private String password;


}
