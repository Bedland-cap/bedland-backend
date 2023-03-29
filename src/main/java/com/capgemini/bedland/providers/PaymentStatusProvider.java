package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.PaymentStatusDto;

import java.util.List;

public interface PaymentStatusProvider {

    List<PaymentStatusDto> getAll();

    PaymentStatusDto getById(Long id);

}
