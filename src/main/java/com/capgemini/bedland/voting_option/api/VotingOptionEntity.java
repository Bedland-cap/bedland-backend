package com.capgemini.bedland.voting_option.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.voting.api.VotingEntity;
import com.capgemini.bedland.voting_response.api.VotingResponseEntity;
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
