package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.BuildingDto;

import java.util.List;

public interface BuildingProvider {

    List<BuildingDto> getAll();

    BuildingDto getById(Long id);

    byte[] getPhotoByBuildingId(Long id);

}
