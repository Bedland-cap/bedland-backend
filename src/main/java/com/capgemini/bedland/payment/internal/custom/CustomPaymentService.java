package com.capgemini.bedland.payment.internal.custom;

import java.util.List;

public interface CustomPaymentService {

    List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(Long managerId);

}
