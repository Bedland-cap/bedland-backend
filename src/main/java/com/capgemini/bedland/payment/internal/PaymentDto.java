package com.capgemini.bedland.payment.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PaymentDto extends AbstractDto {

    private Long flatId;

    private LocalDateTime expirationDate;

    private String paymentType;

    private double paymentValue;
}
