package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.BuildingDto;
import org.springframework.web.multipart.MultipartFile;

public interface BuildingService {

    BuildingDto create(BuildingDto request);

    void delete(Long id);

    BuildingDto update(BuildingDto request);

    BuildingDto updatePhoto(Long id, MultipartFile file);

}
