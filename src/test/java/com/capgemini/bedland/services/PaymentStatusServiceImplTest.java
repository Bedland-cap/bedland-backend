package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.PaymentStatusDto;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.PaymentStatusRepository;
import com.capgemini.bedland.services.impl.PaymentStatusServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class PaymentStatusServiceImplTest {

    @Autowired
    PaymentStatusServiceImpl paymentStatusService;
    @Autowired
    PaymentStatusRepository paymentStatusRepository;

    @Test
    void shouldReturnAllPaymentStatusWhenGetAll() {
        //given
        PaymentStatusDto paymentStatus1 = paymentStatusService.getById(1L);
        PaymentStatusDto paymentStatus2 = paymentStatusService.getById(2L);
        List<PaymentStatusDto> expected = List.of(paymentStatus1, paymentStatus2);
        //when
        List<PaymentStatusDto> resultList = paymentStatusService.getAll();
        //then
        assertTrue(resultList.containsAll(expected));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnPaymentStatusWhenGetPaymentStatusById() {
        //given
        Long paymentStatusId = 2L;
        PaymentStatusDto paymentStatus = PaymentStatusDto.builder()
                .id(paymentStatusId)
                .paymentId(1L)
                .paymentStatusName("PAID")
                .build();
        //when
        PaymentStatusDto result = paymentStatusService.getById(paymentStatusId);
        //then
        assertEquals(result, paymentStatus);
    }

    @Test
    void shouldReturnThrowWhenGetPaymentStatusByIdAndPaymentStatusNotExist() {
        assertThrows(NotFoundException.class, () -> paymentStatusService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetPaymentStatusByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.getById(null));
    }

    @Test
    void shouldReturnCreatedPaymentStatusWhenCreatePaymentStatus() {
        //given
        PaymentStatusDto paymentStatus = new PaymentStatusDto();
        paymentStatus.setPaymentId(1L);
        paymentStatus.setPaymentStatusName(PaymentStatusName.UNPAID.toString());
        //when
        PaymentStatusDto createPaymentStatus = paymentStatusService.create(paymentStatus);
        //then
        assertNotNull(createPaymentStatus);
        assertNotNull(createPaymentStatus.getId());
        assertTrue(paymentStatusRepository.existsById(createPaymentStatus.getId()));
    }

    @Test
    void shouldReturnCreatedPaymentStatusWithPaymentStatusNameUnpaidWhenCreatePaymentStatusAndPaymentStatusNameIsNotSpecified() {
        //given
        PaymentStatusDto paymentStatus = new PaymentStatusDto();
        paymentStatus.setPaymentId(1L);
        //when
        PaymentStatusDto createPaymentStatus = paymentStatusService.create(paymentStatus);
        //then
        assertEquals("UNPAID", createPaymentStatus.getPaymentStatusName());
    }

    @Test
    void shouldReturnThrowWhenCreatePaymentStatusAndGivenPaymentStatusHasId() {
        //given
        PaymentStatusDto paymentStatus = new PaymentStatusDto();
        paymentStatus.setPaymentId(1L);
        paymentStatus.setId(100L);
        //when+then
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.create(paymentStatus));
    }

    @Test
    void shouldReturnThrowWhenCreatePaymentStatusAndGivenPaymentStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.create(null));
    }

    @Test
    void shouldCreatePaymentStatusWithNameUnpaidWhenCreateByPaymentId() {
        //given
        List<PaymentStatusDto> beforePaymentStatus = paymentStatusService.getAll();
        String expectedName = "UNPAID";
        Long paymentId = 1L;
        //when
        paymentStatusService.createByPaymentId(paymentId);
        List<PaymentStatusDto> afterPaymentStatus = paymentStatusService.getAll();
        PaymentStatusDto newPaymentStatus = paymentStatusService.getById(paymentStatusService.getAll()
                .get(afterPaymentStatus.size() - 1)
                .getId());
        //then
        assertEquals(1, afterPaymentStatus.size() - beforePaymentStatus.size());
        assertSame(expectedName, newPaymentStatus.getPaymentStatusName());
        assertSame(paymentId, newPaymentStatus.getPaymentId());
    }

    @Test
    void shouldReturnThrowWhenCreateByPaymentIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.createByPaymentId(null));
    }

    @Test
    void shouldReturnThrowWhenCreateByPaymentIdAndPaymentWithGivenIdNotExist() {
        assertThrows(NotFoundException.class, () -> paymentStatusService.createByPaymentId(Long.MAX_VALUE));
    }

    @Test
    void shouldDeletePaymentStatusWhenDeleteById() {
        //given
        List<PaymentStatusDto> beforePaymentStatus = paymentStatusService.getAll();
        Long id = beforePaymentStatus.get(0).getId();
        //when
        paymentStatusService.delete(id);
        List<PaymentStatusDto> afterPaymentStatus = paymentStatusService.getAll();
        //then
        assertEquals(1, beforePaymentStatus.size() - afterPaymentStatus.size());
        assertFalse(paymentStatusRepository.existsById(id));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.delete(null));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenPaymentStatusNotExist() {
        assertThrows(NotFoundException.class, () -> paymentStatusService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnUpdatedPaymentStatusWhenUpdate() {
        //given
        PaymentStatusDto paymentStatus = paymentStatusService.getById(1L);
        String updatePaymentStatusName = "PAID";
        PaymentStatusDto updatePaymentStatus = PaymentStatusDto.builder()
                .id(paymentStatus.getId())
                .version(paymentStatus.getVersion())
                .createDate(paymentStatus.getCreateDate())
                .updateDate(paymentStatus.getUpdateDate())
                .paymentId(paymentStatus.getPaymentId())
                .paymentStatusName(updatePaymentStatusName)
                .build();
        //when
        paymentStatusService.update(updatePaymentStatus);
        PaymentStatusDto resultPaymentStatus = paymentStatusService.getById(1L);
        //then
        assertEquals(updatePaymentStatus.getId(), resultPaymentStatus.getId());
        assertEquals(resultPaymentStatus.getPaymentStatusName(), updatePaymentStatusName);
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentStatusHasNoId() {
        assertThrows(IllegalArgumentException.class, () -> paymentStatusService.update(new PaymentStatusDto()));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentStatusNotExist() {
        //given
        PaymentStatusDto paymentStatus = paymentStatusService.getById(1L);
        String updatePaymentStatusName = "PAID";
        PaymentStatusDto updatePaymentStatus = PaymentStatusDto.builder()
                .id(100L)
                .version(paymentStatus.getVersion())
                .createDate(paymentStatus.getCreateDate())
                .updateDate(paymentStatus.getUpdateDate())
                .paymentId(paymentStatus.getPaymentId())
                .paymentStatusName(updatePaymentStatusName)
                .build();
        //when+then
        assertThrows(NotFoundException.class, () -> paymentStatusService.update(updatePaymentStatus));
    }

}