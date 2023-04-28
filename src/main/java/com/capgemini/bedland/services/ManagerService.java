package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.ManagerDto;
import org.springframework.web.multipart.MultipartFile;

public interface ManagerService {

    ManagerDto create(ManagerDto request);

    void delete(Long id);

    ManagerDto update(ManagerDto request);

    ManagerDto updateAvatar(Long id, MultipartFile file);

}
