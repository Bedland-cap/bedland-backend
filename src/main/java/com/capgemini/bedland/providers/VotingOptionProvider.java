package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.VotingOptionDto;

import java.util.List;

public interface VotingOptionProvider {

    List<VotingOptionDto> getAll();

    VotingOptionDto getById(Long id);

}
