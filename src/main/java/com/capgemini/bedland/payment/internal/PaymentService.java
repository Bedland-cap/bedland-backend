package com.capgemini.bedland.payment.internal;

interface PaymentService {

    PaymentDto create(PaymentDto request);

    void delete(Long id);

    PaymentDto update(PaymentDto request);

}
