package com.capgemini.bedland.building.internal;

import org.springframework.web.multipart.MultipartFile;

interface BuildingService {

    BuildingDto create(BuildingDto request);

    void delete(Long id);

    BuildingDto update(BuildingDto request);

    BuildingDto updatePhoto(Long id, MultipartFile file);

}
