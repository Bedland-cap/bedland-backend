package com.capgemini.bedland.payment.internal.custom;

import com.capgemini.bedland.payment.api.PaymentEntity;
import com.capgemini.bedland.payment.api.QPaymentEntity;
import com.capgemini.bedland.payment_status.api.PaymentStatusEntity;
import com.capgemini.bedland.payment_status.api.QPaymentStatusEntity;
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
}
