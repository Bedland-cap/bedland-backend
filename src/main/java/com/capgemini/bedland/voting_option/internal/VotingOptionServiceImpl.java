package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.voting.internal.VotingRepository;
import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import com.capgemini.bedland.voting_option.api.VotingOptionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class VotingOptionServiceImpl implements VotingOptionService, VotingOptionProvider {

    @Autowired
    private VotingOptionRepository votingOptionRepository;
    @Autowired
    private VotingOptionMapper votingOptionMapper;
    @Autowired
    private VotingRepository votingRepository;

    @Override
    public List<VotingOptionDto> getAll() {
        return votingOptionMapper.entities2DTO(votingOptionRepository.findAll());
    }

    @Override
    public VotingOptionDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return votingOptionMapper.entity2Dto(votingOptionRepository.findById(id)
                                                                   .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public VotingOptionDto create(VotingOptionDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. VotingOption can't be created");
        }
        VotingOptionEntity createVotingOption = votingOptionRepository.save(repackDtoToEntity(request));
        return votingOptionMapper.entity2Dto(createVotingOption);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!votingOptionRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        votingOptionRepository.deleteById(id);
    }

    @Override
    public VotingOptionDto update(VotingOptionDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!votingOptionRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        VotingOptionEntity updateVotingOption = votingOptionRepository.save(repackDtoToEntity(request));
        return votingOptionMapper.entity2Dto(updateVotingOption);
    }

    private VotingOptionEntity repackDtoToEntity(VotingOptionDto dto) {
        VotingOptionEntity entity = votingOptionMapper.dto2Entity(dto);
        entity.setVotingEntity(votingRepository.findById(dto.getVotingId())
                                               .orElseThrow(() -> new NotFoundException(dto.getVotingId())));
        return entity;
    }

}
