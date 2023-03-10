package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.building.internal.BuildingRepository;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.flat.api.FlatProvider;
import com.capgemini.bedland.member.internal.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class FlatServiceImpl implements FlatService, FlatProvider {

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
