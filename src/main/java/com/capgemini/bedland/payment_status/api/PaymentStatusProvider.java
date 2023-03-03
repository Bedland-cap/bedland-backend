package com.capgemini.bedland.payment_status.api;

import com.capgemini.bedland.payment_status.internal.PaymentStatusDto;

import java.util.List;

public interface PaymentStatusProvider {

    List<PaymentStatusDto> getAll();

    PaymentStatusDto getById(Long id);

}
