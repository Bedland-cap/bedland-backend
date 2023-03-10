package com.capgemini.bedland.voting_response.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private VotingOptionEntity votingOptionEntity;

}
