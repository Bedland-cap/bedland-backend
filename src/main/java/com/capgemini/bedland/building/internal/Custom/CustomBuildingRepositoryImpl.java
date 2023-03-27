package com.capgemini.bedland.building.internal.Custom;

import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.building.api.QBuildingEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomBuildingRepositoryImpl implements CustomBuildingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<BuildingEntity> findAllBuildingsForGivenManager(Long managerId) {
        QBuildingEntity buildingEntity = QBuildingEntity.buildingEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(buildingEntity)
                .join(buildingEntity.managerEntity)
                .where(buildingEntity.managerEntity.id.eq(managerId))
                .fetch();
    }
}
