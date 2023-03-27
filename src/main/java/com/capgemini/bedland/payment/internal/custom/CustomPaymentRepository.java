package com.capgemini.bedland.payment.internal.custom;

import com.capgemini.bedland.payment.api.PaymentEntity;
import com.capgemini.bedland.payment_status.api.PaymentStatusEntity;

import java.util.List;

public interface CustomPaymentRepository {


    List<PaymentEntity> findAllPaymentsForGivenBuilding(Long buildingId);

    List<PaymentStatusEntity> findAllStatusesForGivenPayment(Long paymentId);
}
