package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.BuildingDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.BuildingMapper;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.services.CustomBuildingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class CustomBuildingServiceImpl implements CustomBuildingService {

    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private BuildingMapper buildingMapper;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<BuildingDto> getBuildingsByManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if(!managerRepository.existsById(managerId)){
            throw new NotFoundException("Manager doesn't exist in DB");
        }
        return buildingMapper.entities2DTOs(buildingRepository.getBuildingsByManager(managerId));
    }
}
