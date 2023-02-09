package com.capgemini.bedland.votingResponse.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.voting.api.VotingEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "VOTING_RESPONSE")
public class VotingResponseEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private VotingEntity votingEntity;

    @Column(nullable = false, name = "ANSWER")
    private int answer;
}
