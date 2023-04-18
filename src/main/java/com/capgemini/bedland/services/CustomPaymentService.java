package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.PaymentSummaryDto;

import java.util.List;

public interface CustomPaymentService {

    List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(Long managerId);

    List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(Long ownerId);
}
