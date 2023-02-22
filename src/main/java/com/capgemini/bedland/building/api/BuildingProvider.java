package com.capgemini.bedland.building.api;

import com.capgemini.bedland.building.internal.BuildingDto;

import java.util.List;

public interface BuildingProvider {
    List<BuildingDto> getAll();

    BuildingDto getById(Long id);

}
