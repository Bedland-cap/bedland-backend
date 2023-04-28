package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.ManagerDto;

import java.util.List;

public interface ManagerProvider {

    List<ManagerDto> getAll();

    ManagerDto getById(Long id);

    byte[] getAvatarByManagerId(Long id);

}
