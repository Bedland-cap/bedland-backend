package com.capgemini.bedland.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "VOTING_OPTION")
public class VotingOptionEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private VotingEntity votingEntity;

    @OneToMany(mappedBy = "votingOptionEntity", cascade = CascadeType.ALL)
    private List<VotingResponseEntity> votingResponseEntities;

    @Column(nullable = false, length = 125, name = "TITLE")
    private String title;

}
