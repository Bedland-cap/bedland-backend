package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

}
