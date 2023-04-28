package com.capgemini.bedland.services;

import com.capgemini.bedland.dataPreparation.EntityPreparator;
import com.capgemini.bedland.dtos.PaymentSummaryDto;
import com.capgemini.bedland.entities.*;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class CustomPaymentServiceImplTest {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private CustomPaymentService customPaymentService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private PaymentStatusRepository paymentStatusRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Test
    void shouldfindAllPaymentsWithStatusesAndTheirAmountsForGivenManager() {
        //given
        ManagerEntity managerEntity1 = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        ManagerEntity managerEntity2 = managerRepository.save(EntityPreparator.prepareSecondTestManager());

        BuildingEntity buildingEntity1 = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity1));
        BuildingEntity buildingEntity2 = buildingRepository.save(EntityPreparator.prepareSecondTestBuilding(managerEntity2));

        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        OwnerEntity ownerEntity2 = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());

        FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity1, ownerEntity1));
        FlatEntity flatEntity2 = flatRepository.save(EntityPreparator.prepareSecondTestFlat(buildingEntity2, ownerEntity2));

        PaymentEntity paymentEntity1 = paymentRepository.save(EntityPreparator.prepareFirstPayment(flatEntity1));
        PaymentEntity paymentEntity2 = paymentRepository.save(EntityPreparator.prepareSecondPayment(flatEntity1));
        PaymentEntity paymentEntity3 = paymentRepository.save(EntityPreparator.prepareThirdPayment(flatEntity2));

        paymentStatusRepository.save(EntityPreparator.preparePAIDPaymentStatus(paymentEntity1));
        paymentStatusRepository.save(EntityPreparator.prepareEXPIREDPaymentStatus(paymentEntity1));
        paymentStatusRepository.save(EntityPreparator.prepareUNPAIDPaymentStatus(paymentEntity2));
        paymentStatusRepository.save(EntityPreparator.prepareUNPAIDPaymentStatus(paymentEntity3));

        List<PaymentSummaryDto> expectedResult = List.of(new PaymentSummaryDto(PaymentStatusName.UNPAID, 1),
                new PaymentSummaryDto(PaymentStatusName.PAID, 0),
                new PaymentSummaryDto(PaymentStatusName.EXPIRED, 1));
        //when
        List<PaymentSummaryDto> allPaymentsWithStatusesAndTheirAmountsForGivenManager = customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(managerEntity1.getId());
        //then
        assertEquals(expectedResult, allPaymentsWithStatusesAndTheirAmountsForGivenManager);
        assertFalse(allPaymentsWithStatusesAndTheirAmountsForGivenManager.isEmpty());

    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenfindingAllPaymentsWithStatusesAndTheirAmountsForGivenManagerAndMangerIDIsNull() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(null));
    }

    @Test
    void shoulThrowdNotFoundExceptionWhenFindingAllPaymentsWithStatusesAndTheirAmountsForGivenManagerAndMangerIDIsNotInDB() {
        //given
        //when
        //then
        assertThrows(NotFoundException.class, () -> customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(999L));
    }

    @Test
    void shouldReturnEmptySummaryWhenfindingAllPaymentsWithStatusesAndTheirAmountsForGivenManagerAndThereAreNoPaymentsForGivenManager() {
        //given
        ManagerEntity managerEntity1 = managerRepository.save(EntityPreparator.prepareFirstTestManager());

        List<PaymentSummaryDto> expectedResult = List.of(new PaymentSummaryDto(PaymentStatusName.UNPAID, 0),
                new PaymentSummaryDto(PaymentStatusName.PAID, 0),
                new PaymentSummaryDto(PaymentStatusName.EXPIRED, 0));
        //when
        List<PaymentSummaryDto> allPaymentsWithStatusesAndTheirAmountsForGivenManager = customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(managerEntity1.getId());
        //then
        assertEquals(expectedResult, allPaymentsWithStatusesAndTheirAmountsForGivenManager);
        assertFalse(allPaymentsWithStatusesAndTheirAmountsForGivenManager.isEmpty());
    }

    @Test
    void shouldFindAllPaymentsWithStatusesAndTheirAmountsForGivenOwner() {
        //given
        OwnerEntity exampleOwner = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());
        BuildingEntity building = buildingRepository.findAll().get(0);
        FlatEntity flat = flatRepository.save(EntityPreparator.prepareFirstTestFlat(building, exampleOwner));
        paymentRepository.save(EntityPreparator.prepareThirdPayment(flat));
        paymentRepository.save(EntityPreparator.prepareSecondPayment(flat));
        paymentRepository.save(EntityPreparator.prepareFirstPayment(flat));
        List<PaymentSummaryDto> expectedPaymentsSummary = new ArrayList<>(
                List.of(new PaymentSummaryDto(PaymentStatusName.UNPAID, 3),
                        new PaymentSummaryDto(PaymentStatusName.PAID, 0),
                        new PaymentSummaryDto(PaymentStatusName.EXPIRED, 0)));
        //when
        List<PaymentSummaryDto> foundPaymentsSummary = customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(exampleOwner.getId());
        //then
        assertFalse(foundPaymentsSummary.isEmpty());
        assertEquals(expectedPaymentsSummary, foundPaymentsSummary);
    }
    @Test
    void shouldThrowNotFoundExceptionWhenFindingAllPaymentsWithStatusesAndOwnerIDIsNotInDB() {
        //given
        //when
        //then
        assertThrows(NotFoundException.class, () -> customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(999L));
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingAllPaymentsWithStatusesAndOwnerIDIsNull() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(null));
    }
}
