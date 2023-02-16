package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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
                              //TODO add after complete flat and building implementation
//                              .flatId(entity.getFlatEntity()
//                                            .getId())
//                              .buildingId(entity.getBuildingEntity()
//                                                .getId())
                              .title(entity.getTitle())
                              .description(entity.getDescription())
                              .build();
    }

    public List<AnnouncementDto> entities2Dtos(List<AnnouncementEntity> entities){
        return entities.stream().map(this::entity2Dto).toList();
    }

    public AnnouncementEntity dto2Entity(AnnouncementDto dto){
        if(dto==null){
            return null;
        }
        AnnouncementEntity newAnnouncement = new AnnouncementEntity();
        newAnnouncement.setVersion(dto.getVersion());
        newAnnouncement.setCreateDate(dto.getCreateDate());
        newAnnouncement.setUpdateDate(dto.getUpdateDate());
//TODO        newAnnouncement.setFlatEntity(null);
//TODO        newAnnouncement.setBuildingEntity(null);
        newAnnouncement.setTitle(dto.getTitle());
        newAnnouncement.setDescription(dto.getDescription());
        if (dto.getId() != null) {
            newAnnouncement.setId(dto.getId());
        }
        return newAnnouncement;
    }

}
