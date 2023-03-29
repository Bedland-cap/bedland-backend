package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.FlatDto;
import com.capgemini.bedland.entities.FlatEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlatMapper {

    public FlatDto entity2Dto(FlatEntity entity) {
        if (entity == null) {
            return null;
        }
        return FlatDto.builder()
                      .id(entity.getId())
                      .version(entity.getVersion())
                      .createDate(entity.getCreateDate())
                      .updateDate(entity.getUpdateDate())
                      .buildingId(entity.getBuildingEntity()
                                        .getId())
                      .number(entity.getNumber())
                      .floor(entity.getFloor())
                      .shapePath(entity.getShapePath())
                      .build();
    }

    public List<FlatDto> entities2DTOs(List<FlatEntity> entities){
        return entities.stream().map(this::entity2Dto).toList();
    }

    public FlatEntity dto2Entity(FlatDto dto){
        if(dto==null){
            return null;
        }
        FlatEntity newFlat  = new FlatEntity();
        newFlat.setVersion(dto.getVersion());
        newFlat.setCreateDate(dto.getCreateDate());
        newFlat.setUpdateDate(dto.getUpdateDate());
        newFlat.setBuildingEntity(null);
        newFlat.setNumber(dto.getNumber());
        newFlat.setFloor(dto.getFloor());
        newFlat.setShapePath(dto.getShapePath());
        if (dto.getId() != null) {
            newFlat.setId(dto.getId());
        }
        return newFlat;
    }

}
