package com.capgemini.bedland.admin.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
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

    @Column(nullable = false, length = 50, name = "LOGIN")
    private String login;

    @Column(nullable = false, length = 50, name = "PASSWORD")
    private String password;


}
