package com.capgemini.bedland.payment.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.payment_status.internal.PaymentStatusDto;
import com.capgemini.bedland.payment_status.internal.PaymentStatusServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class PaymentServiceImplTest {

    @Autowired
    PaymentServiceImpl paymentService;
    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    PaymentStatusServiceImpl paymentStatusService;

    @Test
    void shouldReturnAllPaymentWhenGetAllPayment() {
        //given
        PaymentDto payment1 = paymentService.getById(1L);
        PaymentDto payment2 = paymentService.getById(2L);
        List<PaymentDto> expected = new ArrayList<>(List.of(payment1,payment2));
        //when
        List<PaymentDto> resultList = paymentService.getAll();
        //then
        assertTrue(resultList.containsAll(expected));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnPaymentWhenGetPaymentById() {
        //given
        Long paymentId = 2L;
        PaymentDto payment = PaymentDto.builder()
                                       .id(paymentId)
                                       .flatId(2L)
                                       .expirationDate(LocalDateTime.of(2023, 03, 03, 00, 00))
                                       .paymentType("MEDIA")
                                       .paymentValue(123.45)
                                       .build();
        //when
        PaymentDto result = paymentService.getById(paymentId);
        //then
        assertEquals(result, payment);
    }

    @Test
    void shouldReturnThrowWhenGetPaymentByIdAndPaymentNotExist() {
        assertThrows(NotFoundException.class, () -> paymentService.getById(Long.MAX_VALUE));
    }
    @Test
    void shouldReturnThrowWhenGetPaymentByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.getById(null));
    }

    @Test
    void shouldReturnCreatedPaymentWhenCreatePayment() {
        //given
        PaymentDto payment = PaymentDto.builder()
                                       .flatId(1L)
                                       .expirationDate(LocalDateTime.now())
                                       .paymentType("RENT")
                                       .paymentValue(300)
                                       .build();
        //when
        PaymentDto createPayment = paymentService.create(payment);
        //then
        assertNotNull(createPayment);
        assertNotNull(createPayment.getId());
        assertTrue(paymentRepository.existsById(createPayment.getId()));
    }

    @Test
    void shouldReturnThrowWhenCreatePaymentAndGivenPaymentHasId() {
        //given
        PaymentDto payment = PaymentDto.builder()
                                       .id(100L)
                                       .flatId(1L)
                                       .expirationDate(LocalDateTime.now())
                                       .paymentType("RENT")
                                       .paymentValue(300)
                                       .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> paymentService.create(payment));
    }
    @Test
    void shouldReturnThrowWhenCreatePaymentAndGivenPaymentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.create(null));
    }

    @Test
    void shouldCreateNewPaymentStatusWhenCreateNewPayment() {
        //given
        PaymentDto payment = PaymentDto.builder()
                                       .flatId(1L)
                                       .expirationDate(LocalDateTime.now())
                                       .paymentType("RENT")
                                       .paymentValue(300)
                                       .build();
        List<PaymentStatusDto> beforePaymentStatusList = paymentStatusService.getAll();
        //when
        PaymentDto createPayment = paymentService.create(payment);
        List<PaymentStatusDto> afterPaymentStatusList = paymentStatusService.getAll();
        PaymentStatusDto newPaymentStatus = paymentStatusService.getById(paymentStatusService.getAll()
                                                                                             .get(afterPaymentStatusList.size() - 1)
                                                                                             .getId());
        //then
        assertEquals(1, afterPaymentStatusList.size() - beforePaymentStatusList.size());
        assertSame(createPayment.getId(), newPaymentStatus.getPaymentId());
    }

    @Test
    void shouldDeletePaymentWhenDeleteById() {
        //given
        List<PaymentDto> beforePayment = paymentService.getAll();
        Long id = beforePayment.get(0).getId();
        //when
        paymentService.delete(id);
        List<PaymentDto> afterPayment = paymentService.getAll();
        //then
        assertFalse(paymentRepository.existsById(id));
        assertEquals(1, beforePayment.size() - afterPayment.size());
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndPaymentNotExist() {
        assertThrows(NotFoundException.class, () -> paymentService.delete(Long.MAX_VALUE));
    }
    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.delete(null));
    }

    @Test
    void shouldReturnUpdatedPaymentWhenUpdate() {
        //given
        PaymentDto payment = paymentService.getById(1L);
        double updatePaymentValue = 10000;
        PaymentDto updatePayment = PaymentDto.builder()
                                             .id(payment.getId())
                                             .version(payment.getVersion())
                                             .createDate(payment.getCreateDate())
                                             .flatId(payment.getFlatId())
                                             .expirationDate(payment.getExpirationDate())
                                             .paymentType(payment.getPaymentType())
                                             .paymentValue(updatePaymentValue)
                                             .build();
        //when
        paymentService.update(updatePayment);
        PaymentDto resultPayment = paymentService.getById(1L);
        //then
        assertEquals(resultPayment.getPaymentValue(), updatePaymentValue);
    }

    @Test
    void shouldReturnThrowWhenUpdatePaymentAndGivenPaymentHasNoId() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.update(new PaymentDto()));
    }
    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentNotExist() {
        //given
        PaymentDto payment = paymentService.getById(1L);
        double updatePaymentValue = 10000;
        PaymentDto updatePayment = PaymentDto.builder()
                                             .id(Long.MAX_VALUE)
                                             .version(payment.getVersion())
                                             .createDate(payment.getCreateDate())
                                             .flatId(payment.getFlatId())
                                             .expirationDate(payment.getExpirationDate())
                                             .paymentType(payment.getPaymentType())
                                             .paymentValue(updatePaymentValue)
                                             .build();
        //when+then
        assertThrows(NotFoundException.class, () -> paymentService.update(updatePayment));
    }

}