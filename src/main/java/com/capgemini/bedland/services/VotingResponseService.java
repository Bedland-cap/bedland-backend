package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingResponseDto;

public interface VotingResponseService {
    VotingResponseDto create(VotingResponseDto request);

    void delete(Long id);

    VotingResponseDto update(VotingResponseDto request);
}
