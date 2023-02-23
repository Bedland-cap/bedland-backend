package com.capgemini.bedland.voting.internal;

interface VotingService {

    VotingDto create(VotingDto request);

    void delete(Long id);

    VotingDto update(VotingDto request);

}
