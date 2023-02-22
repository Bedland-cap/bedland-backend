package com.capgemini.bedland.payment.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.paymentStatus.api.PaymentStatusEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "PAYMENT")
public class PaymentEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @OneToMany(mappedBy = "paymentEntity", cascade = CascadeType.ALL)
    private List<PaymentStatusEntity> paymentStatusEntities;

    @Column(nullable = false, length = 125, name = "EXPIRATION_DATE")
    private LocalDateTime expirationDate;

    @Column(nullable = false, name = "PAYMENT_TYPE")
    private PaymentType paymentType;

}