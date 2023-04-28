package com.capgemini.bedland.entities;

import com.capgemini.bedland.enums.PaymentStatusName;
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

    @Column(nullable = false, length = 50, name = "PAYMENT_STATUS_NAME")
    @Enumerated(EnumType.STRING)
    private PaymentStatusName paymentStatusName;

    @PostPersist
    public void setLastPaymentStatusName() {
        this.paymentEntity.setLastPaymentStatusName(this.paymentStatusName);
    }
}
