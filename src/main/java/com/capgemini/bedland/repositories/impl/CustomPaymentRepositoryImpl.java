package com.capgemini.bedland.repositories.impl;

import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.entities.PaymentStatusEntity;
import com.capgemini.bedland.entities.QPaymentEntity;
import com.capgemini.bedland.entities.QPaymentStatusEntity;
import com.capgemini.bedland.repositories.CustomPaymentRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<PaymentEntity> findAllPaymentsForGivenBuilding(Long buildingId) {

        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(paymentEntity)
                .join(paymentEntity.flatEntity)
                .join(paymentEntity.flatEntity.buildingEntity)
                .where(paymentEntity.flatEntity.buildingEntity.id.eq(buildingId))
                .fetch();
    }

    @Override
    public List<PaymentStatusEntity> findAllStatusesForGivenPayment(Long paymentId) {
        QPaymentStatusEntity paymentStatusEntity = QPaymentStatusEntity.paymentStatusEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(paymentStatusEntity)
                .join(paymentStatusEntity.paymentEntity)
                .where(paymentStatusEntity.paymentEntity.id.eq(paymentId))
                .fetch();

    }

    @Override
    public PaymentStatusEntity findLatestStatusForGivenPayment(Long paymentId) {

        QPaymentStatusEntity paymentStatusEntity = QPaymentStatusEntity.paymentStatusEntity;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        return queryFactory.selectFrom(paymentStatusEntity)
                .join(paymentStatusEntity.paymentEntity)
                .where(paymentStatusEntity.paymentEntity.id.eq(paymentId))
                .orderBy(paymentStatusEntity.createDate.desc())
                .fetchFirst();
    }
}
