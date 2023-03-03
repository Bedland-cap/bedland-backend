package com.capgemini.bedland.payment_status.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentStatusDto extends AbstractDto {

    private Long paymentId;
    private String paymentStatusName;

}
