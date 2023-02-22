package com.capgemini.bedland.flat.api;

import com.capgemini.bedland.flat.internal.FlatDto;

import java.util.List;

public interface FlatProvider {

    List<FlatDto> getAll();

    FlatDto getById(Long id);

}
