package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.OwnerDto;

import java.util.List;

public interface OwnerProvider {

    List<OwnerDto> getAll();

    OwnerDto getById(Long id);

    byte[] getAvatarByOwnerId(Long id);
}
