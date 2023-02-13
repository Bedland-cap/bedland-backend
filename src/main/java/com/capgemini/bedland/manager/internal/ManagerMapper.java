package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ManagerMapper {

    public ManagerDto entity2Dto(ManagerEntity entity) {
        return ManagerDto.builder()
                .id(entity.getId())
                .login(entity.getLogin())
                .password(entity.getPassword())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .phoneNumber(entity.getPhoneNumber())
                .build();
    }

    public List<ManagerDto> entities2Dtos(List<ManagerEntity> entities) {
        return entities.stream().map(this::entity2Dto).toList();
    }

    public ManagerEntity dto2Entity(final ManagerDto dto) {
        ManagerEntity newManager = new ManagerEntity();

        newManager.setLogin(dto.getLogin());
        newManager.setPassword(dto.getPassword());
        newManager.setName(dto.getName());
        newManager.setLastName(dto.getLastName());
        newManager.setEmail(dto.getEmail());
        newManager.setPhoneNumber(dto.getPhoneNumber());

/*                .login(dto.getLogin())
                .password(dto.getPassword())
                .name(dto.getName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .build();*/

        if(dto.getId() != null)
            newManager.setId(dto.getId());

        return newManager;
    }

}
