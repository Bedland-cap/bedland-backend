package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.VotingResponseDto;
import com.capgemini.bedland.entities.VotingResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VotingResponseMapper {

    public VotingResponseDto entity2Dto(VotingResponseEntity entity) {
        if (entity == null) {
            return null;
        }
        return VotingResponseDto.builder()
                                .id(entity.getId())
                                .version(entity.getVersion())
                                .createDate(entity.getCreateDate())
                                .updateDate(entity.getUpdateDate())
                                .flatId(entity.getFlatEntity()
                                              .getId())
                                .votingOptionId(entity.getVotingOptionEntity()
                                                      .getId())
                                .build();
    }

    public List<VotingResponseDto> entities2DTO(List<VotingResponseEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public VotingResponseEntity dto2Entity(VotingResponseDto dto) {
        if (dto == null) {
            return null;
        }
        VotingResponseEntity newVotingResponse = new VotingResponseEntity();
        newVotingResponse.setVersion(dto.getVersion());
        newVotingResponse.setCreateDate(dto.getCreateDate());
        newVotingResponse.setUpdateDate(dto.getUpdateDate());
        newVotingResponse.setFlatEntity(null);
        newVotingResponse.setVotingOptionEntity(null);
        if (dto.getId() != null) {
            newVotingResponse.setId(dto.getId());
        }
        return newVotingResponse;
    }

}
