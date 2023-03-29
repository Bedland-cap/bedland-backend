package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.VotingDto;

import java.util.List;

public interface VotingProvider {

    List<VotingDto> getAll();

    VotingDto getById(Long id);

}
