package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.FlatDto;

import java.util.List;

public interface FlatProvider {

    List<FlatDto> getAll();

    FlatDto getById(Long id);

}
