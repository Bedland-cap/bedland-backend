package com.capgemini.bedland.paymentStatus.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.payment.api.PaymentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "PAYMENT_STATUS")
public class PaymentStatusEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private PaymentEntity paymentEntity;

    @Column(nullable = false, length = 125, name = "PAYMENT_STATUS_NAME")
    private PaymentStatusName paymentStatusName;

}
