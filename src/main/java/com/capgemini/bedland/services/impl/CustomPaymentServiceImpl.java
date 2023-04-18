package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.PaymentSummaryDto;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.repositories.PaymentRepository;
import com.capgemini.bedland.services.CustomPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class CustomPaymentServiceImpl implements CustomPaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID can't be null");
        }
        if (!managerRepository.existsById(managerId)) {
            throw new NotFoundException("Manager ID doesn't exist in DB");
        }
        List<PaymentEntity> allPaymentsForGivenManager = paymentRepository.findAllPaymentsForGivenManager(managerId);
        return buildPaymentsSummaryDtosForGivenPayments(allPaymentsForGivenManager);
    }

    @Override
    public List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(Long ownerId) {
        if (ownerId == null) {
            throw new IllegalArgumentException("Owner ID can't be null");
        }
        if (!ownerRepository.existsById(ownerId)) {
            throw new NotFoundException("Owner ID doesn't exist in DB");
        }
       return buildPaymentsSummaryDtosForGivenPayments(getPaymentsForActualMonthForGivenOwner(ownerId));
    }

    private List<PaymentSummaryDto> buildPaymentsSummaryDtosForGivenPayments(List<PaymentEntity> payments) {

        List<PaymentSummaryDto> paymentSummaryDtos = new LinkedList<>();
        List<PaymentStatusName> paymentStatusNames = Arrays.stream(PaymentStatusName.values()).toList();

        for (PaymentStatusName statusName : paymentStatusNames) {
            PaymentSummaryDto paymentSummaryDto = new PaymentSummaryDto();
            int paymentStatusAmount;
            if (payments.isEmpty()) {
                paymentStatusAmount = 0;
            } else {
                paymentStatusAmount = payments.stream().filter(e -> e.getLastPaymentStatusName().equals(statusName)).toList().size();
            }
            paymentSummaryDto.setPaymentStatusName(statusName);
            paymentSummaryDto.setAmountOfPayments(paymentStatusAmount);
            paymentSummaryDtos.add(paymentSummaryDto);
        }
        return paymentSummaryDtos;
    }

    private List<PaymentEntity> getPaymentsForActualMonthForGivenOwner(Long ownerId) {
        return paymentRepository.findAllPaymentsForGivenOwner(ownerId)
                .stream()
                .filter(payment -> payment
                        .getCreateDate().getMonth().equals(LocalDateTime.now().getMonth()))
                .toList();
    }
}
