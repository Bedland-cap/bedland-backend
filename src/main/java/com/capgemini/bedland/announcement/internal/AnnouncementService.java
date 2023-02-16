package com.capgemini.bedland.announcement.internal;

interface AnnouncementService {

    AnnouncementDto create(AnnouncementDto request);

    void delete(Long id);

    AnnouncementDto update(AnnouncementDto request);

}
