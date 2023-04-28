package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.VotingResponseDto;

import java.util.List;

public interface VotingResponseProvider {
    List<VotingResponseDto> getAll();

    VotingResponseDto getById(Long id);
}
