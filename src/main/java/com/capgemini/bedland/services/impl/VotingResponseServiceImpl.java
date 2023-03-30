package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.VotingResponseDto;
import com.capgemini.bedland.entities.VotingResponseEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.VotingResponseMapper;
import com.capgemini.bedland.providers.VotingResponseProvider;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.VotingOptionRepository;
import com.capgemini.bedland.repositories.VotingResponseRepository;
import com.capgemini.bedland.services.VotingResponseService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Transactional
@Service
public class VotingResponseServiceImpl implements VotingResponseService, VotingResponseProvider {

    @Autowired
    private VotingResponseRepository votingResponseRepository;
    @Autowired
    private VotingResponseMapper votingResponseMapper;
    @Autowired
    private VotingOptionRepository votingOptionRepository;
    @Autowired
    private FlatRepository flatRepository;

    @Override
    public List<VotingResponseDto> getAll() {
        return votingResponseMapper.entities2DTO(votingResponseRepository.findAll());
    }

    @Override
    public VotingResponseDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return votingResponseMapper.entity2Dto(votingResponseRepository.findById(id)
                                                                       .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public VotingResponseDto create(VotingResponseDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. VotingResponse can't be created");
        }
        VotingResponseEntity createVotingResponse = votingResponseRepository.save(repackDtoToEntity(request));
        return votingResponseMapper.entity2Dto(createVotingResponse);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!votingResponseRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        votingResponseRepository.deleteById(id);
    }

    @Override
    public VotingResponseDto update(VotingResponseDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!votingResponseRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        VotingResponseEntity updateVotingResponse = votingResponseRepository.save(repackDtoToEntity(request));
        return votingResponseMapper.entity2Dto(updateVotingResponse);
    }

    private VotingResponseEntity repackDtoToEntity(VotingResponseDto dto) {
        VotingResponseEntity entity = votingResponseMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                                           .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        entity.setVotingOptionEntity(votingOptionRepository.findById(dto.getVotingOptionId())
                                                           .orElseThrow(() -> new NotFoundException(dto.getVotingOptionId())));
        return entity;
    }

}
