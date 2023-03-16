package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.api.VotingEntity;

import java.util.List;

public interface CustomVotingRepository {

    List<VotingEntity> findAllVotingsForGivenManager(Long managerId);
}
