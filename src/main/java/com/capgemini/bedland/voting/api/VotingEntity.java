package com.capgemini.bedland.voting.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "VOTING")
public class VotingEntity extends AbstractEntity {


    @OneToMany(mappedBy = "votingEntity", cascade = CascadeType.ALL)
    private List<VotingOptionEntity> votingOptionEntities;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private BuildingEntity buildingEntity;

    @Column(nullable = false, length = 125, name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(nullable = false, length = 125, name = "TITLE")
    private String title;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

}
