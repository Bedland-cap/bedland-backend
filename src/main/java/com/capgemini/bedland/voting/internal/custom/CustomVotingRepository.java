package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.api.VotingEntity;
import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import com.capgemini.bedland.voting_response.api.VotingResponseEntity;

import java.util.List;

public interface CustomVotingRepository {

    List<VotingEntity> findAllVotingsForGivenManager(Long managerId);

    List<VotingResponseEntity> findResponsesForGivenVotingOption(Long votingOptionId);
    List<VotingOptionEntity> findOptionsForGivenVoting(Long votingId);

}
