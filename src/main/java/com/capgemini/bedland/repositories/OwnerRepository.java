package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.OwnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerRepository extends JpaRepository<OwnerEntity, Long> {

    @Query("select o from OwnerEntity o " +
            "join o.flatEntities f " +
            "join f.buildingEntity b " +
            "join b.managerEntity m where m.id =:managerId")
    List<OwnerEntity> findAllOwnerForGivenManager(@Param("managerId") Long managerId);

}
