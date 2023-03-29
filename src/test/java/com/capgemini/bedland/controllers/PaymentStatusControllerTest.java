package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.PaymentStatusDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.PaymentStatusProvider;
import com.capgemini.bedland.services.PaymentStatusService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
class PaymentStatusControllerTest {

    private MockMvc mockMvc;
    @Mock
    private PaymentStatusService paymentStatusServiceMock;
    @Mock
    private PaymentStatusProvider paymentStatusProviderMock;

    @BeforeEach
    void setUpController() {
        PaymentStatusController controller = new PaymentStatusController(paymentStatusServiceMock,
                paymentStatusProviderMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnListOfPaymentStatusWhenGetAll() throws Exception {
        PaymentStatusDto paymentStatus1 = new PaymentStatusDto(1L, "UNPAID");
        PaymentStatusDto paymentStatus2 = new PaymentStatusDto(1L, "PAID");

        when(paymentStatusProviderMock.getAll()).thenReturn(List.of(paymentStatus1, paymentStatus2));

        mockMvc.perform(get("/payment-status").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].paymentId").value(paymentStatus1.getPaymentId()))
                .andExpect(jsonPath("$[0].paymentStatusName").value(paymentStatus1.getPaymentStatusName()))
                .andExpect(jsonPath("$[1].paymentId").value(paymentStatus2.getPaymentId()))
                .andExpect(jsonPath("$[1].paymentStatusName").value(paymentStatus2.getPaymentStatusName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllAndZeroPaymentStatusExist() throws Exception {
        mockMvc.perform(get("/payment-status").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnPaymentStatusWhenGetPaymentStatusById() throws Exception {
        PaymentStatusDto paymentStatus = new PaymentStatusDto(1L, "UNPAID");
        Long paymentStatusId = 1L;
        when(paymentStatusProviderMock.getById(any())).thenReturn(paymentStatus);
        mockMvc.perform(get("/payment-status/{paymentStatusId}", paymentStatusId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(paymentStatus.getPaymentId()))
                .andExpect((jsonPath("$.paymentStatusName").value(paymentStatus.getPaymentStatusName())));
    }

    @Test
    void shouldReturnNotFoundWhenGetPaymentStatusByIdAndPaymentStatusNoExist() throws Exception {
        Long paymentStatusId = 1L;

        doThrow(NotFoundException.class).when(paymentStatusProviderMock)
                .getById(paymentStatusId);

        mockMvc.perform(get("/payment-status/{paymentStatusId}", paymentStatusId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void shouldReturnCreatedPaymentStatusWhenCreate() throws Exception {
        Long paymentId = 1L;
        String paymentStatusName = "PAID";
        PaymentStatusDto paymentStatus = new PaymentStatusDto(paymentId, paymentStatusName);

        when(paymentStatusServiceMock.create(any())).thenReturn(paymentStatus);

        String requestBody = """
                {
                   "paymentId": %s,
                   "paymentStatusName": "%s"
                }
                """.formatted(paymentId, paymentStatusName);
        mockMvc.perform(post("/payment-status")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentId").value(paymentStatus.getPaymentId()));
    }

    @Test
    @Transactional
    void shouldDeletedPaymentStatusWhenDelete() throws Exception {
        long paymentStatusId = 1L;

        mockMvc.perform(delete("/payment-status/{paymentStatusId}", paymentStatusId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndPaymentNotExist() throws Exception {
        Long paymentStatusId = 10L;

        doThrow(NotFoundException.class).when(paymentStatusServiceMock)
                .delete(paymentStatusId);

        mockMvc.perform(delete("/payment-status/{paymentStatusId}", paymentStatusId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedPaymentStatusWhenUpdate() throws Exception {
        Long paymentId = 1L;
        String paymentStatusName = "PAID";
        PaymentStatusDto paymentStatus = new PaymentStatusDto(paymentId, paymentStatusName);

        when(paymentStatusServiceMock.update(any())).thenReturn(paymentStatus);

        String updateRequest = """
                {
                   "id": %s,
                   "paymentId": %s,
                   "paymentStatusName": "%s"
                }
                """.formatted(paymentStatus.getId(), paymentId, paymentStatusName);
        mockMvc.perform(patch("/payment-status")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paymentId").value(paymentStatus.getPaymentId()));
    }

}