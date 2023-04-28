package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.entities.VotingEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.VotingMapper;
import com.capgemini.bedland.providers.VotingProvider;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.VotingRepository;
import com.capgemini.bedland.services.VotingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class VotingServiceImpl implements VotingService, VotingProvider {

    @Autowired
    private VotingRepository votingRepository;
    @Autowired
    private VotingMapper votingMapper;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<VotingDto> getAll() {
        return votingMapper.entities2DTO(votingRepository.findAll());
    }

    @Override
    public VotingDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return votingMapper.entity2Dto(votingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public VotingDto create(VotingDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Voting can't be created");
        }
        VotingEntity createVoting = votingRepository.save(repackDtoToEntity(request));
        return votingMapper.entity2Dto(createVoting);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!votingRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        votingRepository.deleteById(id);
    }

    @Override
    public VotingDto update(VotingDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!votingRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        VotingEntity updateVoting = votingRepository.save(repackDtoToEntity(request));
        return votingMapper.entity2Dto(updateVoting);
    }

    private VotingEntity repackDtoToEntity(VotingDto dto) {
        VotingEntity entity = votingMapper.dto2Entity(dto);
        entity.setBuildingEntity(buildingRepository.findById(dto.getBuildingId())
                .orElseThrow(() -> new NotFoundException(dto.getBuildingId())));
        return entity;
    }

}