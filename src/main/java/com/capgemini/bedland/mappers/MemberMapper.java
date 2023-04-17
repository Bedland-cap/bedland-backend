package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.MemberDto;
import com.capgemini.bedland.entities.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MemberMapper {

    public MemberDto entity2Dto(MemberEntity entity) {
        if (entity == null) {
            return null;
        }
        return MemberDto.builder()
                        .id(entity.getId())
                        .version(entity.getVersion())
                        .createDate(entity.getCreateDate())
                        .updateDate(entity.getUpdateDate())
                        .name(entity.getName())
                        .lastName(entity.getLastName())
                        .email(entity.getEmail())
                        .phoneNumber(entity.getPhoneNumber())
                        .flatId(entity.getFlatEntity()
                                      .getId())
                        .avatar(entity.getAvatar())
                        .build();
    }

    public List<MemberDto> entities2DTOs(List<MemberEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public MemberEntity dto2Entity(MemberDto dto) {
        if (dto == null) {
            return null;
        }
        MemberEntity newMember = new MemberEntity();
        newMember.setVersion(dto.getVersion());
        newMember.setCreateDate(dto.getCreateDate());
        newMember.setUpdateDate(dto.getUpdateDate());
        newMember.setFlatEntity(null);
        newMember.setName(dto.getName());
        newMember.setLastName(dto.getLastName());
        newMember.setEmail(dto.getEmail());
        newMember.setPhoneNumber(dto.getPhoneNumber());
        newMember.setAvatar(dto.getAvatar());
        if (dto.getId() != null) {
            newMember.setId(dto.getId());
        }
        return newMember;
    }

}
