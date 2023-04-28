package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingOptionDto;

public interface VotingOptionService {

    VotingOptionDto create(VotingOptionDto request);

    void delete(Long id);

    VotingOptionDto update(VotingOptionDto request);

}
