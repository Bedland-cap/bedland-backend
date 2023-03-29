package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.entities.IncidentStatusEntity;
import com.capgemini.bedland.enums.IncidentStatusName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentStatusMapper {

    public IncidentStatusDto entity2Dto(IncidentStatusEntity entity) {
        if (entity == null) {
            return null;
        }
        return IncidentStatusDto.builder()
                                .id(entity.getId())
                                .version(entity.getVersion())
                                .createDate(entity.getCreateDate())
                                .updateDate(entity.getUpdateDate())
                                .incidentId(entity.getIncidentEntity()
                                                  .getId())
                                .incidentStatusName(entity.getIncidentStatusName()
                                                          .toString())
                                .build();
    }

    public List<IncidentStatusDto> entities2DTOs(List<IncidentStatusEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public IncidentStatusEntity dto2Entity(IncidentStatusDto dto) {
        if (dto == null) {
            return null;
        }
        IncidentStatusEntity newIncidentStatus = new IncidentStatusEntity();
        newIncidentStatus.setVersion(dto.getVersion());
        newIncidentStatus.setCreateDate(dto.getCreateDate());
        newIncidentStatus.setUpdateDate(dto.getUpdateDate());
        if (dto.getIncidentStatusName() == null) {
            newIncidentStatus.setIncidentStatusName(IncidentStatusName.CREATED);
        } else {
            newIncidentStatus.setIncidentStatusName(IncidentStatusName.valueOf(dto.getIncidentStatusName()));
        }
        newIncidentStatus.setIncidentEntity(null);
        if (dto.getId() != null) {
            newIncidentStatus.setId(dto.getId());
        }
        return newIncidentStatus;
    }

}
