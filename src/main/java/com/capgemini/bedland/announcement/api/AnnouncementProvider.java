package com.capgemini.bedland.announcement.api;

import com.capgemini.bedland.announcement.internal.AnnouncementDto;

import java.util.List;

public interface AnnouncementProvider {
    List<AnnouncementDto> getAll();

    AnnouncementDto getById(Long id);

}
