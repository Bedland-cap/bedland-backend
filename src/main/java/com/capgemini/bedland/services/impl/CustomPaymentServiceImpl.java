package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.PaymentSummaryDto;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.PaymentRepository;
import com.capgemini.bedland.services.CustomPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    public List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID can't be null");
        }
        if (!managerRepository.existsById(managerId)) {
            throw new NotFoundException("Manager ID doesn't exist in DB");
        }

        return buildPaymentsSummaryDtosFromGivenPaymentStatuses(managerId);
    }

    private List<PaymentSummaryDto> buildPaymentsSummaryDtosFromGivenPaymentStatuses(Long managerId) {
        List<PaymentEntity> allPaymentsForGivenManager = paymentRepository.findAllPaymentsForGivenManager(managerId);
        List<PaymentSummaryDto> paymentSummaryDtos = new LinkedList<>();
        List<PaymentStatusName> paymentStatusNames = Arrays.stream(PaymentStatusName.values()).toList();

        for (PaymentStatusName statusName : paymentStatusNames) {
            PaymentSummaryDto paymentSummaryDto = new PaymentSummaryDto();
            int paymentStatusAmount;
            if (allPaymentsForGivenManager.isEmpty()) {
                paymentStatusAmount = 0;
            } else {
                paymentStatusAmount = allPaymentsForGivenManager.stream().filter(e -> e.getLastPaymentStatusName().equals(statusName)).toList().size();
            }
            paymentSummaryDto.setPaymentStatusName(statusName);
            paymentSummaryDto.setAmountOfPayments(paymentStatusAmount);
            paymentSummaryDtos.add(paymentSummaryDto);
        }
        return paymentSummaryDtos;
    }
}
