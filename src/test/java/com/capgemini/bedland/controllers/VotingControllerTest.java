package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.VotingProvider;
import com.capgemini.bedland.services.VotingService;
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
class VotingControllerTest {

    private MockMvc mockMvc;
    @Mock
    private VotingService votingServiceMock;
    @Mock
    private VotingProvider votingProviderMock;

    @BeforeEach
    void setUpController() {
        VotingController controller = new VotingController(votingServiceMock, votingProviderMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnListOfVotingWhenGetAll() throws Exception {
        VotingDto voting1 = VotingDto.builder()
                .buildingId(1L)
                .expirationDate(LocalDateTime.now())
                .title("Title")
                .description("Description")
                .build();
        VotingDto voting2 = VotingDto.builder()
                .expirationDate(LocalDateTime.now())
                .buildingId(1L)
                .title("Title")
                .description("Description")
                .build();

        when(votingProviderMock.getAll()).thenReturn(List.of(voting1, voting2));

        mockMvc.perform((get("/voting").contentType(APPLICATION_JSON)))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].buildingId").value(voting1.getBuildingId()))
                .andExpect(jsonPath("$[0].title").value(voting1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(voting1.getDescription()))
                .andExpect(jsonPath("$[1].buildingId").value(voting2.getBuildingId()))
                .andExpect(jsonPath("$[1].title").value(voting2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(voting2.getDescription()))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllVotingAndZeroVotingExist() throws Exception {
        mockMvc.perform(get("/voting").contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnVotingWhenGetVotingById() throws Exception {
        VotingDto voting = VotingDto.builder()
                .buildingId(1L)
                .expirationDate(LocalDateTime.now())
                .title("Title")
                .description("Description")
                .build();
        Long votingId = 1L;

        when(votingProviderMock.getById(votingId)).thenReturn(voting);

        mockMvc.perform(get("/voting/{votingId}", votingId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.buildingId").value(voting.getBuildingId()))
                .andExpect(jsonPath("$.title").value(voting.getTitle()))
                .andExpect(jsonPath("$.description").value(voting.getDescription()));
    }

    @Test
    void shouldReturnNotFoundWhenGetVotingByIdAndVotingNoExist() throws Exception {
        Long votingId = 1L;

        doThrow(NotFoundException.class).when(votingProviderMock)
                .getById(votingId);

        mockMvc.perform(get("/voting/{votingId}", votingId).contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedVotingWhenCreate() throws Exception {
        Long buildingId = 1L;
        LocalDateTime expirationDate = LocalDateTime.now();
        String title = "title";
        String description = "Description";
        VotingDto voting = VotingDto.builder()
                .buildingId(buildingId)
                .expirationDate(expirationDate)
                .title(title)
                .description(description)
                .build();

        when(votingServiceMock.create(any())).thenReturn(voting);

        String requestBody = """
                {
                   "buildingId": %s,
                   "expirationDate": "%s",
                   "title": "%s",
                   "description": "%s"
                }
                """.formatted(buildingId, expirationDate, title, description);
        mockMvc.perform(post("/voting")
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.buildingId").value(voting.getBuildingId()))
                .andExpect(jsonPath("$.title").value(voting.getTitle()))
                .andExpect(jsonPath("$.description").value(voting.getDescription()));
    }

    @Test
    void shouldDeletedVotingWhenDelete() throws Exception {
        VotingDto voting = VotingDto.builder()
                .id(1L)
                .buildingId(1L)
                .expirationDate(LocalDateTime.now())
                .title("Title")
                .description("Description")
                .build();

        mockMvc.perform(delete("/voting/{votingId}", voting.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndVotingNotExist() throws Exception {
        Long votingId = 1L;

        doThrow(NotFoundException.class).when(votingServiceMock)
                .delete(votingId);

        mockMvc.perform(delete("/voting/{votingId}", votingId)
                        .contentType(APPLICATION_JSON))
                .andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedVotingWhenUpdate() throws Exception {
        Long id = 1L;
        Long buildingId = 1L;
        LocalDateTime expirationDate = LocalDateTime.now();
        String title = "title";
        String description = "Description";
        VotingDto voting = VotingDto.builder()
                .id(id)
                .buildingId(buildingId)
                .expirationDate(expirationDate)
                .title(title)
                .description(description)
                .build();

        when(votingServiceMock.update(any())).thenReturn(voting);

        String updateRequest = """
                {
                   "id": %s,
                   "buildingId": %s,
                   "expirationDate": "%s",
                   "title": "%s",
                   "description": "%s"
                }
                """.formatted(id, buildingId, expirationDate, title, description);
        mockMvc.perform(patch("/voting")
                        .contentType(APPLICATION_JSON)
                        .content(updateRequest))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(voting.getId()))
                .andExpect(jsonPath("$.buildingId").value(voting.getBuildingId()))
                .andExpect(jsonPath("$.title").value(voting.getTitle()))
                .andExpect(jsonPath("$.description").value(voting.getDescription()));
    }

}