package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.building.api.BuildingEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class BuildingMapper {

    public BuildingDto entity2Dto(BuildingEntity entity) {
        if (entity == null) {
            return null;
        }
        return BuildingDto.builder()
                          .id(entity.getId())
                          .version(entity.getVersion())
                          .createDate(entity.getCreateDate())
                          .updateDate(entity.getUpdateDate())
                          .managerId(entity.getManagerEntity()
                                           .getId())
                          .buildingName(entity.getBuildingName())
                          .address(entity.getAddress())
                          .floors(entity.getFloors())
                          .photo(entity.getPhoto())
                          .build();
    }

    public List<BuildingDto> entities2DTOs(List<BuildingEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public BuildingEntity dto2Entity(BuildingDto dto) {
        if (dto == null) {
            return null;
        }
        BuildingEntity newBuilding = new BuildingEntity();
        newBuilding.setVersion(dto.getVersion());
        newBuilding.setCreateDate(dto.getCreateDate());
        newBuilding.setUpdateDate(dto.getUpdateDate());
        newBuilding.setManagerEntity(null);
        newBuilding.setBuildingName(dto.getBuildingName());
        newBuilding.setAddress(dto.getAddress());
        newBuilding.setFloors(dto.getFloors());
        newBuilding.setPhoto(dto.getPhoto());
        if (dto.getId() != null) {
            newBuilding.setId(dto.getId());
        }
        return newBuilding;
    }

}
