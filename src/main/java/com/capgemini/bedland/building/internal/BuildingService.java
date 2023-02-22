package com.capgemini.bedland.building.internal;

interface BuildingService {

    BuildingDto create(BuildingDto request);

    void delete(Long id);

    BuildingDto update(BuildingDto request);

}
