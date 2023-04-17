package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.FlatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FlatRepository extends JpaRepository<FlatEntity, Long> {
    @Query("select f from FlatEntity  f " +
            "join f.flatMembers fm " +
            "join f.buildingEntity b " +
            "join b.managerEntity m where m.id=:managerId and b.id=:buildingId order by f.number asc")
    List<FlatEntity> findFlatsForGivenManagerInGivenBuilding(@Param("managerId") Long managerId, @Param("buildingId") Long buildingId);

    @Query("select f from FlatEntity f " +
            "join f.flatOwnerEntity o " +
            "join f.buildingEntity b " +
            "join f.announcementEntities a " +
            "join b.managerEntity ma where ma.id =:managerId and a.toManager=true group by f")
    List<FlatEntity> findFlatsWhichOwnersMadeAnnouncementToManager(@Param("managerId") Long managerId);
}
