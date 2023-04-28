package com.capgemini.bedland.repositories.impl;

import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.repositories.CustomPaymentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findLatestPaymentsForGivenManagerWithGivenLastStatus(Long managerId, int numberOfPayments, PaymentStatusName paymentStatusName) {
        return entityManager.createQuery("select p from PaymentEntity p " +
                        "join p.flatEntity f " +
                        "join f.buildingEntity b " +
                        "join b.managerEntity m where m.id =" + managerId + " " +
                        "and p.lastPaymentStatusName=" + paymentStatusName.toString() + " " +
                        "group by p order by p.updateDate desc ")
                .setMaxResults(numberOfPayments).getResultList();

    }

    @Override
    public List findLatestPaymentsForGivenOwnerWithGivenLastStatus(Long ownerId, int numberOfPayments, PaymentStatusName paymentStatusName) {
        return entityManager.createQuery("select p from PaymentEntity p " +
                        "join p.flatEntity f " +
                        "join f.flatOwnerEntity fo where fo.id =" + ownerId + " " +
                        "and p.lastPaymentStatusName='EXPIRED' or p.lastPaymentStatusName='UNPAID' " +
                        "group by p order by p.createDate desc ")
                .setMaxResults(numberOfPayments).getResultList();
    }

}
