package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.BuildingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Long> {
    @Query("select b from BuildingEntity b join b.managerEntity m where m.id =:managerId")
    List<BuildingEntity> getBuildingsByManager(@Param("managerId") Long managerId);

}
