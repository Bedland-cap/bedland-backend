package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.voting.api.VotingEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VotingMapper {

    public VotingDto entity2Dto(VotingEntity entity) {
        if (entity == null) {
            return null;
        }
        return VotingDto.builder()
                        .id(entity.getId())
                        .version(entity.getVersion())
                        .createDate(entity.getCreateDate())
                        .updateDate(entity.getUpdateDate())
                        .buildingId(entity.getBuildingEntity()
                                          .getId())
                        .expirationDate(entity.getExpirationDate())
                        .title(entity.getTitle())
                        .description(entity.getDescription())
                        .build();
    }

    public List<VotingDto> entities2DTO(List<VotingEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }
    public VotingEntity dto2Entity(VotingDto dto){
        if(dto == null){
            return null;
        }
        VotingEntity newVoting = new VotingEntity();
        newVoting.setVersion(dto.getVersion());
        newVoting.setCreateDate(dto.getCreateDate());
        newVoting.setUpdateDate(dto.getUpdateDate());
        newVoting.setBuildingEntity(null);
        newVoting.setExpirationDate(dto.getExpirationDate());
        newVoting.setTitle(dto.getTitle());
        newVoting.setDescription(dto.getDescription());
        if (dto.getId() != null) {
            newVoting.setId(dto.getId());
        }
        return newVoting;
    }

}
