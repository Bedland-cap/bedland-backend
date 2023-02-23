package com.capgemini.bedland.voting.api;

import com.capgemini.bedland.voting.internal.VotingDto;

import java.util.List;

public interface VotingProvider {

    List<VotingDto> getAll();

    VotingDto getById(Long id);

}
