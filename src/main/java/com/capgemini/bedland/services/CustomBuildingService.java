package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.BuildingDto;

import java.util.List;

public interface CustomBuildingService {

    List<BuildingDto> getBuildingsByManager(Long managerId);

}
