package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.PaymentStatusName;

import java.util.List;

public interface CustomPaymentRepository {

    List<PaymentEntity> findLatestPaymentsForGivenManagerWithGivenLastStatus(Long managerId, int numberOfPayments, PaymentStatusName paymentStatusName);

    List<PaymentEntity> findLatestPaymentsForGivenOwnerWithGivenLastStatus(Long ownerId, int numberOfPayments, PaymentStatusName paymentStatusName);


}
