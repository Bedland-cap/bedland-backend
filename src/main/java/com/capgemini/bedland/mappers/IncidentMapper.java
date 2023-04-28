package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.IncidentDto;
import com.capgemini.bedland.entities.IncidentEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class IncidentMapper {

    public IncidentDto entity2Dto(IncidentEntity entity) {
        if (entity == null) {
            return null;
        }
        return IncidentDto.builder()
                          .id(entity.getId())
                          .version(entity.getVersion())
                          .createDate(entity.getCreateDate())
                          .updateDate(entity.getUpdateDate())
                          .flatId(entity.getFlatEntity()
                                        .getId())
                          .title(entity.getTitle())
                          .description(entity.getDescription())
                          .commonSpace(entity.isCommonSpace())
                          .build();
    }

    public List<IncidentDto> entities2DTOs(List<IncidentEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public IncidentEntity dto2Entity(IncidentDto dto) {
        if (dto == null) {
            return null;
        }
        IncidentEntity newIncident = new IncidentEntity();

        newIncident.setVersion(dto.getVersion());
        newIncident.setCreateDate(dto.getCreateDate());
        newIncident.setUpdateDate(dto.getUpdateDate());
        newIncident.setFlatEntity(null);
        newIncident.setTitle(dto.getTitle());
        newIncident.setDescription(dto.getDescription());
        newIncident.setCommonSpace(dto.isCommonSpace());
        if (dto.getId() != null) {
            newIncident.setId(dto.getId());
        }
        return newIncident;
    }
}
