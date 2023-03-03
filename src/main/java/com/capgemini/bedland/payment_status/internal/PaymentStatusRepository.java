package com.capgemini.bedland.payment_status.internal;

import com.capgemini.bedland.payment_status.api.PaymentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface PaymentStatusRepository extends JpaRepository<PaymentStatusEntity, Long> {

}
