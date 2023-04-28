package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.FlatDto;

public interface FlatService {

    FlatDto create(FlatDto request);

    void delete(Long id);

    FlatDto update(FlatDto request);

}
