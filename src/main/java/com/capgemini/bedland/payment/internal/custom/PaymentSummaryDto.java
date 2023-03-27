package com.capgemini.bedland.payment.internal.custom;

import com.capgemini.bedland.payment_status.api.PaymentStatusName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentSummaryDto {

    private PaymentStatusName paymentStatusName;
    private int amountOfPayments;
}
