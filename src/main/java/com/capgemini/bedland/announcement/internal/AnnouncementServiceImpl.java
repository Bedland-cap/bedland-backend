package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import com.capgemini.bedland.announcement.api.AnnouncementProvider;
import com.capgemini.bedland.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class AnnouncementServiceImpl implements AnnouncementService, AnnouncementProvider {

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<AnnouncementDto> getAll() {
        return announcementMapper.entities2Dtos(announcementRepository.findAll());
    }

    @Override
    public AnnouncementDto getById(Long id) {
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
        if(!announcementRepository.existsById(id)){
            throw new NotFoundException(id);
        }
        announcementRepository.deleteById(id);

    }

    @Override
    public AnnouncementDto update(AnnouncementDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if(!announcementRepository.existsById(request.getId())){
            throw new NotFoundException(request.getId());
        }
        AnnouncementEntity updateAnnouncement = announcementRepository.save(repackDtoToEntity(request));
        return announcementMapper.entity2Dto(updateAnnouncement);
    }

    private AnnouncementEntity repackDtoToEntity(AnnouncementDto dto) {
        AnnouncementEntity entity = announcementMapper.dto2Entity(dto);
        //TODO add after complete flat implementation
        // entity.setFlatEntity(...);
        // entity.setBuildingEntity(...);
        return entity;
    }

}
