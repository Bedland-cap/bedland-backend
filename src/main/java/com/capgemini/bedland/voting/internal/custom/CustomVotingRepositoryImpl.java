package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.api.QVotingEntity;
import com.capgemini.bedland.voting.api.VotingEntity;
import com.capgemini.bedland.voting_option.api.QVotingOptionEntity;
import com.capgemini.bedland.voting_option.api.VotingOptionEntity;
import com.capgemini.bedland.voting_response.api.QVotingResponseEntity;
import com.capgemini.bedland.voting_response.api.VotingResponseEntity;
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
    //todo: tests for below
    @Override
    public List<VotingResponseEntity> findResponsesForGivenVotingOption(Long votingOptionId) {
        QVotingResponseEntity votingResponseEntity = QVotingResponseEntity.votingResponseEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(votingResponseEntity)
                .join(votingResponseEntity.votingOptionEntity)
                .where(votingResponseEntity.votingOptionEntity.id.eq(votingOptionId)).fetch();
    }

    //todo: tests for below
    @Override
    public List<VotingOptionEntity> findOptionsForGivenVoting(Long votingId) {
        QVotingOptionEntity votingOptionEntity = QVotingOptionEntity.votingOptionEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(votingOptionEntity)
                .join(votingOptionEntity.votingEntity)
                .where(votingOptionEntity.votingEntity.id.eq(votingId)).fetch();
    }



}
