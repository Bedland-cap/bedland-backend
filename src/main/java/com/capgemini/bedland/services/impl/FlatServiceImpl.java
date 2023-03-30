package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.FlatDto;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.FlatMapper;
import com.capgemini.bedland.providers.FlatProvider;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.MemberRepository;
import com.capgemini.bedland.services.FlatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class FlatServiceImpl implements FlatService, FlatProvider {

    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private FlatMapper flatMapper;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public List<FlatDto> getAll() {
        return flatMapper.entities2DTOs(flatRepository.findAll());
    }

    @Override
    public FlatDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return flatMapper.entity2Dto(flatRepository.findById(id)
                                                   .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public FlatDto create(FlatDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Flat can't be created");
        }
        if (request.getFloor() < 0 || request.getFloor() > 20) {
            throw new IllegalArgumentException("Incorrect value for floor");
        }
        FlatEntity createFlat = flatRepository.save(repackDtoToEntity(request));
        return flatMapper.entity2Dto(createFlat);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!flatRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        flatRepository.deleteById(id);
    }

    @Override
    public FlatDto update(FlatDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!flatRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        FlatEntity updateFlat = flatRepository.save(repackDtoToEntity(request));
        return flatMapper.entity2Dto(updateFlat);
    }

    private FlatEntity repackDtoToEntity(FlatDto dto) {
        FlatEntity entity = flatMapper.dto2Entity(dto);
        entity.setBuildingEntity(buildingRepository.findById(dto.getBuildingId())
                                                   .orElseThrow(() -> new NotFoundException(dto.getBuildingId())));
        return entity;
    }

}
