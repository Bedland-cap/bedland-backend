package com.capgemini.bedland.manager.internal;

import org.springframework.web.multipart.MultipartFile;

interface ManagerService {

    ManagerDto create(ManagerDto request);

    void delete(Long id);

    ManagerDto update(ManagerDto request);

    ManagerDto updateAvatar(Long id, MultipartFile file);

}
