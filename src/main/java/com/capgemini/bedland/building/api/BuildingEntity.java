package com.capgemini.bedland.building.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.voting.api.VotingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

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

    @Column(nullable = false, length = 125, name = "BUILDING_NAME")
    private String buildingName;

    @Column(nullable = false, length = 125, name = "ADDRESS")
    private String address;

    @Column(nullable = false, length = 2, name = "FLOORS")
    private int floors;

}
