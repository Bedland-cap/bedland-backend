package com.capgemini.bedland.voting_option.api;

import com.capgemini.bedland.voting_option.internal.VotingOptionDto;

import java.util.List;

public interface VotingOptionProvider {

    List<VotingOptionDto> getAll();

    VotingOptionDto getById(Long id);

}
