package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.building.api.BuildingProvider;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.manager.internal.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BuildingServiceImpl implements BuildingService, BuildingProvider {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<BuildingDto> getAll() {
        return buildingMapper.entities2DTOs(buildingRepository.findAll());
    }

    @Override
    public BuildingDto getById(Long id) {
        return buildingMapper.entity2Dto(buildingRepository.findById(id)
                                                           .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public BuildingDto create(BuildingDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Building can't be created");
        }
        BuildingEntity createBuilding = buildingRepository.save(repackDtoToEntity(request));
        return buildingMapper.entity2Dto(createBuilding);
    }

    @Override
    public void delete(Long id) {
        if (!buildingRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        buildingRepository.deleteById(id);

    }

    @Override
    public BuildingDto update(BuildingDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!buildingRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        BuildingEntity updateBuilding = buildingRepository.save(repackDtoToEntity(request));
        return buildingMapper.entity2Dto(updateBuilding);
    }

    private BuildingEntity repackDtoToEntity(BuildingDto dto) {
        BuildingEntity entity = buildingMapper.dto2Entity(dto);
        entity.setManagerEntity(managerRepository.findById(dto.getManagerId())
                                                 .orElseThrow(() -> new NotFoundException(dto.getManagerId())));
        return entity;
    }

}
