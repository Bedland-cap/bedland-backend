package com.capgemini.bedland.payment_status.internal;

public interface PaymentStatusService {

    PaymentStatusDto create(PaymentStatusDto request);

    void createByPaymentId(Long paymentId);

    void delete(Long id);

    PaymentStatusDto update(PaymentStatusDto request);

}
