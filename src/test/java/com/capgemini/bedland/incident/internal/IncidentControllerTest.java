package com.capgemini.bedland.incident.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.incident.api.IncidentProvider;
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
class IncidentControllerTest {

    private MockMvc mockMvc;
    @Mock
    private IncidentService incidentServiceMock;
    @Mock
    private IncidentProvider incidentProviderMock;

    @BeforeEach
    void setUpControllerTest() {
        IncidentController controller = new IncidentController(incidentProviderMock, incidentServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnListOfIncidentWhenGetAll() throws Exception {
        IncidentDto incident1 = IncidentDto.builder()
                                           .flatId(1L)
                                           .title("Title")
                                           .description("Description")
                                           .commonSpace(true)
                                           .build();
        IncidentDto incident2 = IncidentDto.builder()
                                           .flatId(1L)
                                           .title("Title")
                                           .description("Description")
                                           .commonSpace(true)
                                           .build();

        when(incidentProviderMock.getAll()).thenReturn(List.of(incident1, incident2));

        mockMvc.perform(get("/incident").contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].flatId").value(incident1.getFlatId()))
               .andExpect(jsonPath("$[0].title").value(incident1.getTitle()))
               .andExpect(jsonPath("$[0].description").value(incident1.getDescription()))
               .andExpect(jsonPath("$[0].commonSpace").value(incident1.isCommonSpace()))
               .andExpect(jsonPath("$[1].flatId").value(incident2.getFlatId()))
               .andExpect(jsonPath("$[1].title").value(incident2.getTitle()))
               .andExpect(jsonPath("$[1].description").value(incident2.getDescription()))
               .andExpect(jsonPath("$[1].commonSpace").value(incident2.isCommonSpace()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllIncidentAndZeroIncidentExist() throws Exception {
        mockMvc.perform(get("/incident").contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnIncidentWhenGetIncidentById() throws Exception {
        IncidentDto incident = IncidentDto.builder()
                                          .flatId(1L)
                                          .title("Title")
                                          .description("Description")
                                          .commonSpace(true)
                                          .build();
        Long incidentId = 1L;

        when(incidentProviderMock.getById(incidentId)).thenReturn(incident);

        mockMvc.perform(get("/incident/{incidentId}", incidentId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.flatId").value(incident.getFlatId()))
               .andExpect(jsonPath("$.title").value(incident.getTitle()))
               .andExpect(jsonPath("$.description").value(incident.getDescription()))
               .andExpect(jsonPath("$.commonSpace").value(incident.isCommonSpace()));
    }

    @Test
    void shouldReturnNotFoundWhenGetIncidentByIdAndIncidentNoExist() throws Exception {
        Long incidentId = 1L;

        doThrow(NotFoundException.class).when(incidentProviderMock)
                                        .getById(incidentId);
        mockMvc.perform(get("/incident/{incidentId}", incidentId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedIncidentWhenCreate() throws Exception {
        Long flatId = 1L;
        String title = "Title";
        String description = "Description";
        boolean commonSpace = true;
        IncidentDto incident = IncidentDto.builder()
                                          .flatId(flatId)
                                          .title(title)
                                          .description(description)
                                          .commonSpace(commonSpace)
                                          .build();

        when(incidentServiceMock.create(any())).thenReturn(incident);

        String requestBody = """
                             {
                                "flatId": %s,
                                "title": "%s",
                                "description": "%s",
                                "commonSpace": %s
                             }
                             """.formatted(flatId, title, description, commonSpace);
        mockMvc.perform(post("/incident")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.flatId").value(incident.getFlatId()))
               .andExpect(jsonPath("$.title").value(incident.getTitle()))
               .andExpect(jsonPath("$.description").value(incident.getDescription()))
               .andExpect(jsonPath("$.commonSpace").value(incident.isCommonSpace()));
    }

    @Test
    void shouldDeletedIncidentWhenDelete() throws Exception {
        Long incidentId = 1L;

        mockMvc.perform(delete("/incident/{incidentId}", incidentId)
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndIncidentNotExist() throws Exception {
        Long incidentId = 10L;

        doThrow(NotFoundException.class).when(incidentServiceMock)
                                        .delete(incidentId);

        mockMvc.perform(delete("/incident/{incidentId}", incidentId)
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedIncidentWhenUpdate() throws Exception {
        Long flatId = 1L;
        String title = "Title";
        String description = "Description";
        boolean commonSpace = true;
        IncidentDto incident = IncidentDto.builder()
                                          .flatId(flatId)
                                          .title(title)
                                          .description(description)
                                          .commonSpace(commonSpace)
                                          .build();

        when(incidentServiceMock.update(any())).thenReturn(incident);

        String updateRequest = """
                               {
                                  "id": %s,
                                  "flatId": %s,
                                  "title": "%s",
                                  "description": "%s",
                                  "commonSpace": %s
                               }
                               """.formatted(incident.getId(), flatId, title, description, commonSpace);
        mockMvc.perform(patch("/incident")
                                .contentType(APPLICATION_JSON)
                                .content(updateRequest))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.flatId").value(incident.getFlatId()))
               .andExpect(jsonPath("$.title").value(incident.getTitle()))
               .andExpect(jsonPath("$.description").value(incident.getDescription()))
               .andExpect(jsonPath("$.commonSpace").value(incident.isCommonSpace()));
    }

}