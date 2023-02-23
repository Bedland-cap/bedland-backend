package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.internal.FlatRepository;
import com.capgemini.bedland.voting_option.internal.VotingOptionRepository;
import com.capgemini.bedland.voting_response.api.VotingResponseEntity;
import com.capgemini.bedland.voting_response.api.VotingResponseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class VotingResponseServiceImpl implements VotingResponseService, VotingResponseProvider {

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
        System.out.println("DUPA");
        return votingResponseMapper.entities2DTO(votingResponseRepository.findAll());
    }

    @Override
    public VotingResponseDto getById(Long id) {
        return votingResponseMapper.entity2Dto(votingResponseRepository.findById(id)
                                                                       .orElseThrow(() -> new NotFoundException(id)));    }

    @Override
    public VotingResponseDto create(VotingResponseDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. VotingResponse can't be created");
        }
        VotingResponseEntity createVotingResponse = votingResponseRepository.save(repackDtoToEntity(request));
        return votingResponseMapper.entity2Dto(createVotingResponse);
    }

    @Override
    public void delete(Long id) {
        if (!votingResponseRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        votingResponseRepository.deleteById(id);
    }

    @Override
    public VotingResponseDto update(VotingResponseDto request) {
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
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId()).orElseThrow(()->new NotFoundException(dto.getFlatId())));
        entity.setVotingOptionEntity(votingOptionRepository.findById(dto.getVotingOptionId()).orElseThrow(()-> new NotFoundException(dto.getVotingOptionId())));
        return entity;
    }

}
