package com.capgemini.bedland.manager.api;

import com.capgemini.bedland.manager.internal.ManagerDto;

import java.util.List;

public interface ManagerProvider {

    List<ManagerDto> getAll();

    ManagerDto getById(Long id);

    byte[] getAvatarByManagerId(Long id);

}
