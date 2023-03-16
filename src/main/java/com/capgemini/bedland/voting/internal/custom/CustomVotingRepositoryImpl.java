package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.api.QVotingEntity;
import com.capgemini.bedland.voting.api.VotingEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomVotingRepositoryImpl implements CustomVotingRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<VotingEntity> findAllVotingsForGivenManager(Long managerId) {

        QVotingEntity votingEntity = QVotingEntity.votingEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(votingEntity)
                .join(votingEntity.buildingEntity)
                .where(votingEntity.buildingEntity.managerEntity.id.eq(managerId))
                .fetch();
    }
}
