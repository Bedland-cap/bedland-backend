package com.capgemini.bedland.voting.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "VOTING")
public class VotingEntity extends AbstractEntity {

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
