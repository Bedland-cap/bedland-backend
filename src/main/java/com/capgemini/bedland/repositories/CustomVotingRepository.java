package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.VotingEntity;
import com.capgemini.bedland.entities.VotingOptionEntity;
import com.capgemini.bedland.entities.VotingResponseEntity;

import java.util.List;

public interface CustomVotingRepository {

    List<VotingEntity> findAllVotingsForGivenManager(Long managerId);

    List<VotingResponseEntity> findResponsesForGivenVotingOption(Long votingOptionId);
    List<VotingOptionEntity> findOptionsForGivenVoting(Long votingId);

}
