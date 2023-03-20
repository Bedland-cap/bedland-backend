package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.internal.VotingDto;

import java.util.List;

public interface CustomVotingService {

    List<VotingDto> findAllVotingsForGivenManager(Long managerId);
    List<VotingDetailsDto> findOptionsWithNumberOfResponsesForGivenVoting(Long votingId);
}
