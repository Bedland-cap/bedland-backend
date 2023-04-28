package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.PaymentDto;

import java.util.List;

public interface PaymentProvider {

    List<PaymentDto> getAll();

    PaymentDto getById(Long id);

}
