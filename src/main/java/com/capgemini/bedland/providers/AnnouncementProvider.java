package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.AnnouncementDto;

import java.util.List;

public interface AnnouncementProvider {
    List<AnnouncementDto> getAll();

    AnnouncementDto getById(Long id);

}
