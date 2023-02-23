package com.capgemini.bedland.manager.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "MANAGER")
public class ManagerEntity extends AbstractEntity {

    @OneToMany(mappedBy = "managerEntity", cascade = CascadeType.ALL)
    private List<BuildingEntity> buildingEntities;

    @Column(nullable = false, length = 45, name = "LOGIN")
    private String login;

    @Column(nullable = false, length = 45, name = "PASSWORD")
    private String password;

    @Column(nullable = false, length = 125, name = "NAME")
    private String name;

    @Column(nullable = false, length = 125, name = "LAST_NAME")
    private String lastName;

    @Column(nullable = false, length = 125, name = "EMAIL")
    private String email;

    @Column(nullable = false, length = 25, name = "PHONE_NUMBER")
    private String phoneNumber;


}
