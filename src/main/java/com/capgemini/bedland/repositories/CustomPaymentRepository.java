package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.entities.PaymentStatusEntity;

import java.util.List;

public interface CustomPaymentRepository {


    List<PaymentEntity> findAllPaymentsForGivenBuilding(Long buildingId);

    List<PaymentStatusEntity> findAllStatusesForGivenPayment(Long paymentId);
}
