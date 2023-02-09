package com.capgemini.bedland.voting.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.votingResponse.api.VotingResponseEntity;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private BuildingEntity buildingEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @OneToMany(mappedBy = "votingEntity", cascade = CascadeType.ALL)
    private List<VotingResponseEntity> votingResponseEntities;

    @Column(nullable = false, length = 125, name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(nullable = false, length = 125, name = "TITLE")
    private String title;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

    @Column(nullable = false, name = "OPTIONS")
    private String options;
}
