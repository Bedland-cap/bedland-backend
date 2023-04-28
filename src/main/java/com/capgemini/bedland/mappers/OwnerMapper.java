package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.OwnerDto;
import com.capgemini.bedland.entities.OwnerEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OwnerMapper {

    public OwnerDto entity2Dto(OwnerEntity entity) {
        if (entity == null) {
            return null;
        }
        return OwnerDto.builder()
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
                .avatar(entity.getAvatar())
                .build();
    }

    public List<OwnerDto> entities2DTOs(List<OwnerEntity> entities) {
        return entities.stream()
                .map(this::entity2Dto)
                .toList();
    }

    public OwnerEntity dto2Entity(OwnerDto dto) {
        if (dto == null) {
            return null;
        }
        OwnerEntity newOwner = new OwnerEntity();
        newOwner.setVersion(dto.getVersion());
        newOwner.setCreateDate(dto.getCreateDate());
        newOwner.setUpdateDate(dto.getUpdateDate());
        newOwner.setLogin(dto.getLogin());
        newOwner.setPassword(dto.getPassword());
        newOwner.setName(dto.getName());
        newOwner.setLastName(dto.getLastName());
        newOwner.setEmail(dto.getEmail());
        newOwner.setPhoneNumber(dto.getPhoneNumber());
        newOwner.setAvatar(dto.getAvatar());
        if (dto.getId() != null) {
            newOwner.setId(dto.getId());
        }
        return newOwner;
    }
}
