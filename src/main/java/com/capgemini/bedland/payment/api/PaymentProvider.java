package com.capgemini.bedland.payment.api;

import com.capgemini.bedland.payment.internal.PaymentDto;

import java.util.List;

public interface PaymentProvider {

    List<PaymentDto> getAll();

    PaymentDto getById(Long id);

}
