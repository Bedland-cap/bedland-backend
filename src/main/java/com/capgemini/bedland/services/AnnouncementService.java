package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.AnnouncementDto;

public interface AnnouncementService {

    AnnouncementDto create(AnnouncementDto request);

    void delete(Long id);

    AnnouncementDto update(AnnouncementDto request);

}
