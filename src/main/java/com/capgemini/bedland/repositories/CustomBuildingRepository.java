package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.BuildingEntity;

import java.util.List;

public interface CustomBuildingRepository {

    List<BuildingEntity> findAllBuildingsForGivenManager(Long managerId);

}
