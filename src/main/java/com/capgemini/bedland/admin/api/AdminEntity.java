package com.capgemini.bedland.admin.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "ADMIN")
public class AdminEntity extends AbstractEntity {

    @Column(nullable = false, length = 125, name = "LOGIN")
    private String login;

    @Column(nullable = false, length = 45, name = "PASSWORD")
    private String password;


}
