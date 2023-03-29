package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.PaymentSummaryDto;
import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.entities.PaymentStatusEntity;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.CustomBuildingRepository;
import com.capgemini.bedland.repositories.CustomPaymentRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
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
    private CustomPaymentRepository customPaymentRepository;

    @Autowired
    private CustomBuildingRepository customBuildingRepository;

    @Autowired
    private ManagerRepository managerRepository;
    //todo: tests for below
    @Override
    public List<PaymentSummaryDto> findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID can't be null");
        }
        if(!managerRepository.existsById(managerId)){
            throw new NotFoundException("Manager ID doesn't exist in DB");
        }
        return buildPaymentsSummaryDtosFromGivenPaymentStatuses(getAllPaymentStatusesForGivenPayments(getAllPaymentsForManager(managerId)));
    }

    private List<PaymentEntity> getAllPaymentsForManager(Long managerId){
        List<BuildingEntity> allBuildingsForGivenManager = customBuildingRepository.findAllBuildingsForGivenManager(managerId);
        List<PaymentEntity> allPaymentsForAllBuildings = new LinkedList<>();
        for (BuildingEntity building : allBuildingsForGivenManager) {
            List<PaymentEntity> allPaymentsForOneBuilding = customPaymentRepository.findAllPaymentsForGivenBuilding(building.getId());
            allPaymentsForAllBuildings.addAll(allPaymentsForOneBuilding);
        }
        return   allPaymentsForAllBuildings.stream().distinct().toList();
    }

    private List<PaymentStatusEntity> getAllPaymentStatusesForGivenPayments(List<PaymentEntity> givenPayments){
        List<PaymentStatusEntity> allStatusesForAllPayments = new LinkedList<>();
        for (PaymentEntity payment : givenPayments) {
            List<PaymentStatusEntity> allStatusesForOnePayment = customPaymentRepository.findAllStatusesForGivenPayment(payment.getId());
            allStatusesForAllPayments.addAll(allStatusesForOnePayment);
        }
        return allStatusesForAllPayments.stream().distinct().toList();
    }

    private List<PaymentSummaryDto> buildPaymentsSummaryDtosFromGivenPaymentStatuses(List<PaymentStatusEntity> givenStatuses){
        List<PaymentSummaryDto> paymentSummaryDtos = new LinkedList<>();
        List<PaymentStatusName> paymentStatusNames = Arrays.stream(PaymentStatusName.values()).toList();

        for (PaymentStatusName statusName : paymentStatusNames) {
            PaymentSummaryDto paymentSummaryDto = new PaymentSummaryDto();
            int paymentStatusAmount = givenStatuses.stream().filter(e -> e.getPaymentStatusName().equals(statusName)).toList().size();
            paymentSummaryDto.setPaymentStatusName(statusName);
            paymentSummaryDto.setAmountOfPayments(paymentStatusAmount);
            paymentSummaryDtos.add(paymentSummaryDto);
        }
        return paymentSummaryDtos;
    }
}
