package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingDetailsDto;
import com.capgemini.bedland.dtos.VotingDto;

import java.util.List;

public interface CustomVotingService {

    List<VotingDto> findAllVotingsForGivenManager(Long managerId);
    List<VotingDetailsDto> findOptionsWithNumberOfResponsesForGivenVoting(Long votingId);
}
