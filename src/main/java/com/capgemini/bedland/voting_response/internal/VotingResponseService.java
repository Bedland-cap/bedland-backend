package com.capgemini.bedland.voting_response.internal;

interface VotingResponseService {
    VotingResponseDto create(VotingResponseDto request);

    void delete(Long id);

    VotingResponseDto update(VotingResponseDto request);
}
