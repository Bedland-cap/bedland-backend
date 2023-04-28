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
@Table(name = "BUILDING")
public class BuildingEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private ManagerEntity managerEntity;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL)
    private List<VotingEntity> votingEntities;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL)
    private List<AnnouncementEntity> announcementEntities;

    @OneToMany(mappedBy = "buildingEntity", cascade = CascadeType.ALL)
    private List<FlatEntity> flatEntities;

    @Column(nullable = false, length = 50, name = "BUILDING_NAME")
    private String buildingName;

    @Column(nullable = false, length = 125, name = "ADDRESS")
    private String address;

    @Column(nullable = false, name = "FLOORS")
    private int floors;
    @Column(name = "PHOTO", length = LONG32)
    private byte[] photo;

}
