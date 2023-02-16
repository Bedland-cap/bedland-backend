package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.member.api.MemberEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class MemberMapper {

    public MemberDto entity2Dto(MemberEntity entity) {
        if (entity == null) {
            return null;
        }
        return MemberDto.builder()
                        .id(entity.getId())
                        .version(entity.getVersion())
                        .createDate(entity.getCreateDate())
                        .updateDate(entity.getUpdateDate())
                        .login(entity.getLogin())
                        .password(entity.getPassword())
                        .name(entity.getName())
                        .lastName(entity.getLastName())
                        .email(entity.getEmail())
                        .phoneNumber(entity.getPhoneNumber())
                        .isOwner(entity.isOwner())
                        //TODO   .flatId(entity.getFlatEntity().getId()) add after implemented flatMapper
                        .build();
    }

    public List<MemberDto> entities2Dtos(List<MemberEntity> entities) {
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
        // TODO newMember.setFlatEntity(null);
        newMember.setLogin(dto.getLogin());
        newMember.setPassword(dto.getPassword());
        newMember.setName(dto.getName());
        newMember.setLastName(dto.getLastName());
        newMember.setEmail(dto.getEmail());
        newMember.setPhoneNumber(dto.getPhoneNumber());
        newMember.setOwner(dto.isOwner());
        if (dto.getId() != null) {
            newMember.setId(dto.getId());
        }
        return newMember;
    }

}
