package com.capgemini.bedland.voting_option.internal;

interface VotingOptionService {

    VotingOptionDto create(VotingOptionDto request);

    void delete(Long id);

    VotingOptionDto update(VotingOptionDto request);

}
