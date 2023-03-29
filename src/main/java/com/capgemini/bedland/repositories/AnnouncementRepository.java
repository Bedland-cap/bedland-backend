package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {

}
