package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    @Query("select m from MemberEntity m " +
            "join m.flatEntity f " +
            "join f.buildingEntity b " +
            "join f.announcementEntities a " +
            "join b.managerEntity ma where ma.id =:managerId")
    List<MemberEntity> findMembersByManager(@Param("managerId") Long managerId);
@Query ("select m from MemberEntity m join m.flatEntity f where f.id =:flatId")
    List<MemberEntity> findByFlatId(@Param("flatId") Long flatId);

}
