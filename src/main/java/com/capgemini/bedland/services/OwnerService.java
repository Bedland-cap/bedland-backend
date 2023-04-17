package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.OwnerDto;
import org.springframework.web.multipart.MultipartFile;

public interface OwnerService {

    OwnerDto create(OwnerDto request);

    void delete(Long id);

    OwnerDto update(OwnerDto request);

    OwnerDto updateAvatar(Long id, MultipartFile file);
}
