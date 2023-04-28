package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    @Query("select p from PaymentEntity p " +
            "join p.flatEntity f " +
            "join f.buildingEntity b " +
            "join b.managerEntity m where m.id =:managerId")
    List<PaymentEntity> findAllPaymentsForGivenManager(@Param("managerId") Long managerId);


    @Query("select p from PaymentEntity  p join p.flatEntity f join f.flatOwnerEntity fo where fo.id =:ownerId group by p")
    List<PaymentEntity> findAllPaymentsForGivenOwner(@Param("ownerId") Long ownerId);

}
