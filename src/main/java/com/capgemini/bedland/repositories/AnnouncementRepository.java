package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<AnnouncementEntity, Long> {
    @Query("select a from AnnouncementEntity a join a.flatEntity f join f.flatOwnerEntity fo where fo.id=:ownerId group by a order by a.createDate desc ")
    List<AnnouncementEntity> getAnnouncementsForGivenOwner(@Param("ownerId") Long ownerId);

}
