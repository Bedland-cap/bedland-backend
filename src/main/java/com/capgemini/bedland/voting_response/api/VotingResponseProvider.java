package com.capgemini.bedland.voting_response.api;

import com.capgemini.bedland.voting_response.internal.VotingResponseDto;

import java.util.List;

public interface VotingResponseProvider {
    List<VotingResponseDto> getAll();

    VotingResponseDto getById(Long id);
}
