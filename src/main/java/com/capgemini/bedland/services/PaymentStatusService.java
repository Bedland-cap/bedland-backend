package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.PaymentStatusDto;

public interface PaymentStatusService {

    PaymentStatusDto create(PaymentStatusDto request);

    void createByPaymentId(Long paymentId);

    void delete(Long id);

    PaymentStatusDto update(PaymentStatusDto request);

}
