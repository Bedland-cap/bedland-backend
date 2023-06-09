package com.capgemini.bedland.dtos;

import com.capgemini.bedland.enums.PaymentStatusName;
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
    private PaymentStatusName paymentStatusName;

}
