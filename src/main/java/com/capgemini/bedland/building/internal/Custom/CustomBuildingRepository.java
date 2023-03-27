package com.capgemini.bedland.building.internal.Custom;

import com.capgemini.bedland.building.api.BuildingEntity;

import java.util.List;

public interface CustomBuildingRepository {

    List<BuildingEntity> findAllBuildingsForGivenManager(Long managerId);

}
