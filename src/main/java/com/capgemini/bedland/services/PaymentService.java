package com.capgemini.bedland.services;


import com.capgemini.bedland.dtos.PaymentDto;

public interface PaymentService {

    PaymentDto create(PaymentDto request);

    void delete(Long id);

    PaymentDto update(PaymentDto request);

}
