package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.PaymentDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.PaymentProvider;
import com.capgemini.bedland.services.PaymentService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PaymentService paymentServiceMock;
    @Mock
    private PaymentProvider paymentProviderMock;

    @BeforeEach
    void setUpController() {
        PaymentController controller = new PaymentController(paymentProviderMock, paymentServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnListOfPaymentWhenGetAll() throws Exception {
        PaymentDto payment1 = new PaymentDto(1L, LocalDateTime.now(), "MEDIA", 200);
        PaymentDto payment2 = new PaymentDto(2L, LocalDateTime.now(), "MEDIA", 200);

        when(paymentProviderMock.getAll()).thenReturn(List.of(payment1, payment2));

        mockMvc.perform(get("/payment").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].flatId").value(payment1.getFlatId()))
                .andExpect(jsonPath("$[0].paymentType").value(payment1.getPaymentType()))
                .andExpect(jsonPath("$[0].paymentValue").value(payment1.getPaymentValue()))
                .andExpect(jsonPath("$[1].flatId").value(payment2.getFlatId()))
                .andExpect(jsonPath("$[1].paymentType").value(payment2.getPaymentType()))
                .andExpect(jsonPath("$[1].paymentValue").value(payment2.getPaymentValue()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllPaymentAndZeroPaymentExist() throws Exception {
        mockMvc.perform(get("/payment").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnPaymentWhenGetPaymentById() throws Exception {
        PaymentDto payment = new PaymentDto(1L, LocalDateTime.now(), "MEDIA", 200);
        Long paymentId = 1L;

        when(paymentProviderMock.getById(any())).thenReturn(payment);

        mockMvc.perform(get("/payment/{paymentId}", paymentId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flatId").value(payment.getFlatId()))
                .andExpect(jsonPath("$.paymentType").value(payment.getPaymentType()))
                .andExpect(jsonPath("$.paymentValue").value(payment.getPaymentValue()));
    }

    @Test
    void shouldReturnNotFoundWhenGetPaymentByIdAndPaymentNoExist() throws Exception {
        Long paymentId = 1L;

        doThrow(NotFoundException.class).when(paymentProviderMock)
                .getById(paymentId);
        mockMvc.perform(get("/payment/{paymentId}", paymentId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturnCreatedPaymentWhenCreate() throws Exception {
        Long flatId = 1L;
        LocalDateTime expirationDate = LocalDateTime.now();
        String paymentType = "MEDIA";
        double paymentValue = 200;
        PaymentDto payment = new PaymentDto(flatId, expirationDate, paymentType, paymentValue);

        when(paymentServiceMock.create(any())).thenReturn(payment);

        String requestBody = """
                {
                   "flatId": %s,
                   "expirationDate": "%s",
                   "paymentType": "%s",
                   "paymentValue": %s
                }
                """.formatted(flatId, expirationDate, paymentType, paymentValue);
        mockMvc.perform(post("/payment")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.flatId").value(payment.getFlatId()))
                .andExpect(jsonPath("$.paymentType").value(payment.getPaymentType()))
                .andExpect(jsonPath("$.paymentValue").value(payment.getPaymentValue()));
    }

    @Test
    @Transactional
    void shouldDeletedPaymentWhenDelete() throws Exception {
        long paymentId = 1L;

        mockMvc.perform(delete("/payment/{paymentId}", paymentId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndPaymentNotExist() throws Exception {
        Long paymentId = 10L;

        doThrow(NotFoundException.class).when(paymentServiceMock)
                .delete(paymentId);

        mockMvc.perform(delete("/payment/{paymentId}", paymentId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedPaymentWhenUpdate() throws Exception {
        Long flatId = 1L;
        LocalDateTime expirationDate = LocalDateTime.now();
        String paymentType = "MEDIA";
        double paymentValue = 200;
        PaymentDto payment = new PaymentDto(flatId, expirationDate, paymentType, paymentValue);

        when(paymentServiceMock.update(any())).thenReturn(payment);

        String updateRequest = """
                {
                   "id": %s,
                   "flatId": %s,
                   "expirationDate": "%s",
                   "paymentType": "%s",
                   "paymentValue": %s
                }
                """.formatted(payment.getId(), flatId, expirationDate, paymentType, paymentValue);
        mockMvc.perform(patch("/payment")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flatId").value(payment.getFlatId()))
                .andExpect(jsonPath("$.paymentType").value(payment.getPaymentType()))
                .andExpect(jsonPath("$.paymentValue").value(payment.getPaymentValue()));
    }

}