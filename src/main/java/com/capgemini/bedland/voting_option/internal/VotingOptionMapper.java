package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class VotingOptionMapper {

    public VotingOptionDto entity2Dto(VotingOptionEntity entity) {
        if (entity == null) {
            return null;
        }
        return VotingOptionDto.builder()
                              .id(entity.getId())
                              .version(entity.getVersion())
                              .createDate(entity.getCreateDate())
                              .updateDate(entity.getUpdateDate())
                              .votingId(entity.getVotingEntity()
                                              .getId())
                              .title(entity.getTitle())
                              .build();
    }

    public List<VotingOptionDto> entities2DTO(List<VotingOptionEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public VotingOptionEntity dto2Entity(VotingOptionDto dto) {
        if (dto == null) {
            return null;
        }
        VotingOptionEntity newVotingOption = new VotingOptionEntity();
        newVotingOption.setVersion(dto.getVersion());
        newVotingOption.setCreateDate(dto.getCreateDate());
        newVotingOption.setUpdateDate(dto.getUpdateDate());
        newVotingOption.setVotingEntity(null);
        newVotingOption.setTitle(dto.getTitle());
        if (dto.getId() != null) {
            newVotingOption.setId(dto.getId());
        }
        return newVotingOption;
    }

}
