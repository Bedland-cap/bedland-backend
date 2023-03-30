package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.entities.AnnouncementEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.AnnouncementMapper;
import com.capgemini.bedland.providers.AnnouncementProvider;
import com.capgemini.bedland.repositories.AnnouncementRepository;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.services.AnnouncementService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class AnnouncementServiceImpl implements AnnouncementService, AnnouncementProvider {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public List<AnnouncementDto> getAll() {
        return announcementMapper.entities2DTOs(announcementRepository.findAll());
    }

    @Override
    public AnnouncementDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        return announcementMapper.entity2Dto(announcementRepository.findById(id)
                                                                   .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public AnnouncementDto create(AnnouncementDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Announcement can't be created");
        }
        AnnouncementEntity createdAnnouncement = announcementRepository.save(repackDtoToEntity(request));
        return announcementMapper.entity2Dto(createdAnnouncement);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Given ID is null");
        }
        if (!announcementRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        announcementRepository.deleteById(id);
    }

    @Override
    public AnnouncementDto update(AnnouncementDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!announcementRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        AnnouncementEntity updateAnnouncement = announcementRepository.save(repackDtoToEntity(request));
        return announcementMapper.entity2Dto(updateAnnouncement);
    }

    private AnnouncementEntity repackDtoToEntity(AnnouncementDto dto) {
        AnnouncementEntity entity = announcementMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                                           .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        entity.setBuildingEntity(buildingRepository.findById(dto.getBuildingId())
                                                   .orElseThrow(() -> new NotFoundException(dto.getBuildingId())));
        return entity;
    }

}
