package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.IncidentStatusProvider;
import com.capgemini.bedland.services.IncidentStatusService;
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
class IncidentStatusControllerTest {

    private MockMvc mockMvc;
    @Mock
    private IncidentStatusService incidentStatusServiceMock;
    @Mock
    private IncidentStatusProvider incidentStatusProviderMock;

    @BeforeEach
    void setUpControllerTest() {
        IncidentStatusController controller = new IncidentStatusController(incidentStatusServiceMock,
                incidentStatusProviderMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnListOfIncidentStatusWhenGetAll() throws Exception {
        IncidentStatusDto incidentStatus1 = new IncidentStatusDto(1L, "CREATED");
        IncidentStatusDto incidentStatus2 = new IncidentStatusDto(1L, "IN_PROGRESS");

        when(incidentStatusProviderMock.getAll()).thenReturn(List.of(incidentStatus1, incidentStatus2));

        mockMvc.perform(get("/incident-status").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].incidentId").value(incidentStatus1.getIncidentId()))
                .andExpect(jsonPath("$[0].incidentStatusName").value(incidentStatus1.getIncidentStatusName()))
                .andExpect(jsonPath("$[1].incidentId").value(incidentStatus1.getIncidentId()))
                .andExpect(jsonPath("$[1].incidentStatusName").value(incidentStatus2.getIncidentStatusName()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllAndZeroIncidentStatusExist() throws Exception {
        mockMvc.perform(get("/incident-status").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnIncidentStatusWhenGetIncidentStatusById() throws Exception {
        IncidentStatusDto incidentStatus = new IncidentStatusDto(1L, "CREATED");
        Long incidentStatusId = 1L;

        when(incidentStatusProviderMock.getById(incidentStatusId)).thenReturn(incidentStatus);

        mockMvc.perform(get("/incident-status/{incidentStatusId}", incidentStatusId).contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(incidentStatus.getIncidentId()))
                .andExpect((jsonPath("$.incidentStatusName").value(incidentStatus.getIncidentStatusName())));
    }

    @Test
    void shouldReturnNotFoundWhenGetIncidentStatusByIdAndIncidentStatusNoExist() throws Exception {
        Long incidentStatusId = 1L;

        doThrow(NotFoundException.class).when(incidentStatusProviderMock)
                .getById(incidentStatusId);

        mockMvc.perform(get("/incident-status/{incidentStatusId}", incidentStatusId).contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedIncidentStatusWhenCreate() throws Exception {
        Long incidentId = 1L;
        String incidentStatusName = "CREATED";
        IncidentStatusDto incidentStatus = new IncidentStatusDto(incidentId, incidentStatusName);

        when(incidentStatusServiceMock.create(any())).thenReturn(incidentStatus);

        String requestBody = """
                {
                   "incidentId": %s,
                   "incidentStatusName": "%s"
                }
                """.formatted(incidentId, incidentStatusName);
        mockMvc.perform(post("/incident-status")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.incidentId").value(incidentStatus.getIncidentId()))
                .andExpect(jsonPath("$.incidentStatusName").value(incidentStatus.getIncidentStatusName()));
    }

    @Test
    void shouldDeletedIncidentStatusWhenDelete() throws Exception {
        Long incidentStatusId = 1L;

        mockMvc.perform(delete("/incident-status/{incidentStatusId}", incidentStatusId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndIncidentStatusNotExist() throws Exception {
        Long incidentStatusId = 10L;

        doThrow(NotFoundException.class).when(incidentStatusServiceMock)
                .delete(incidentStatusId);

        mockMvc.perform(delete("/incident-status/{incidentStatusId}", incidentStatusId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedIncidentStatusWhenUpdate() throws Exception {
        Long incidentId = 1L;
        String incidentStatusName = "CREATED";
        IncidentStatusDto incidentStatus = new IncidentStatusDto(incidentId, incidentStatusName);

        when(incidentStatusServiceMock.update(any())).thenReturn(incidentStatus);

        String updateRequest = """
                {
                   "id": %s,
                   "incidentId": %s,
                   "incidentStatusName": "%s"
                }
                """.formatted(incidentStatus.getId(), incidentId, incidentStatusName);
        mockMvc.perform(patch("/incident-status")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.incidentId").value(incidentStatus.getIncidentId()))
                .andExpect(jsonPath("$.incidentStatusName").value(incidentStatus.getIncidentStatusName()));
    }

}