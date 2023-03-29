package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.entities.AnnouncementEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public
class AnnouncementMapper {

    public AnnouncementDto entity2Dto(AnnouncementEntity entity) {
        if (entity == null) {
            return null;
        }
        return AnnouncementDto.builder()
                              .id(entity.getId())
                              .version(entity.getVersion())
                              .createDate(entity.getCreateDate())
                              .updateDate(entity.getUpdateDate())
                              .flatId(entity.getFlatEntity()
                                            .getId())
                              .buildingId(entity.getBuildingEntity()
                                                .getId())
                              .title(entity.getTitle())
                              .description(entity.getDescription())
                              .build();
    }

    public List<AnnouncementDto> entities2DTOs(List<AnnouncementEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public AnnouncementEntity dto2Entity(AnnouncementDto dto) {
        if (dto == null) {
            return null;
        }
        AnnouncementEntity newAnnouncement = new AnnouncementEntity();
        newAnnouncement.setVersion(dto.getVersion());
        newAnnouncement.setCreateDate(dto.getCreateDate());
        newAnnouncement.setUpdateDate(dto.getUpdateDate());
        newAnnouncement.setFlatEntity(null);
        newAnnouncement.setBuildingEntity(null);
        newAnnouncement.setTitle(dto.getTitle());
        newAnnouncement.setDescription(dto.getDescription());
        if (dto.getId() != null) {
            newAnnouncement.setId(dto.getId());
        }
        return newAnnouncement;
    }

}
