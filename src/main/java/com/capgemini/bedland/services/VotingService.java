package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingDto;

public interface VotingService {

    VotingDto create(VotingDto request);

    void delete(Long id);

    VotingDto update(VotingDto request);

}
