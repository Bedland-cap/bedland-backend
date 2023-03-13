package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.voting_response.api.VotingResponseProvider;
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
class VotingResponseControllerTest {

    private MockMvc mockMvc;
    @Mock
    private VotingResponseService votingResponseServiceMock;
    @Mock
    private VotingResponseProvider votingResponseProviderMock;

    @BeforeEach
    void setUpController() {
        VotingResponseController controller = new VotingResponseController(votingResponseProviderMock,
                                                                           votingResponseServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnListOfVotingResponseWhenGetAll() throws Exception {
        VotingResponseDto votingResponse1 = VotingResponseDto.builder()
                                                             .votingOptionId(1L)
                                                             .flatId(1L)
                                                             .build();
        VotingResponseDto votingResponse2 = VotingResponseDto.builder()
                                                             .votingOptionId(2L)
                                                             .flatId(2L)
                                                             .build();
        when(votingResponseProviderMock.getAll()).thenReturn(List.of(votingResponse1, votingResponse2));

        mockMvc.perform((get("/voting-response").contentType(APPLICATION_JSON)))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].flatId").value(votingResponse1.getFlatId()))
               .andExpect(jsonPath("$[0].votingOptionId").value(votingResponse1.getVotingOptionId()))
               .andExpect(jsonPath("$[1].flatId").value(votingResponse2.getFlatId()))
               .andExpect(jsonPath("$[1].votingOptionId").value(votingResponse2.getVotingOptionId()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllVotingResponseAndZeroVotingResponseExist() throws Exception {
        mockMvc.perform(get("/voting-response").contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnVotingResponseWhenGetVotingResponseById() throws Exception {
        Long votingResponseId = 1L;
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .id(votingResponseId)
                                                            .votingOptionId(1L)
                                                            .flatId(1L)
                                                            .build();

        when(votingResponseProviderMock.getById(votingResponseId)).thenReturn(votingResponse);

        mockMvc.perform(get("/voting-response/{votingResponseId}", votingResponseId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.flatId").value(votingResponse.getFlatId()))
               .andExpect(jsonPath("$.votingOptionId").value(votingResponse.getVotingOptionId()));
    }

    @Test
    void shouldReturnNotFoundWhenGetVotingResponseByIdAndVotingResponseNoExist() throws Exception {
        Long votingResponseId = 1L;

        doThrow(NotFoundException.class).when(votingResponseProviderMock)
                                        .getById(votingResponseId);

        mockMvc.perform(get("/voting-response/{votingResponseId}", votingResponseId).contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedVotingResponseWhenCreate() throws Exception {
        Long votingOptionId = 1L;
        Long flatId = 1L;
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .votingOptionId(votingOptionId)
                                                            .flatId(flatId)
                                                            .build();

        when(votingResponseServiceMock.create(any())).thenReturn(votingResponse);

        String requestBody = """
                             {
                                "votingOptionId": %s,
                                "flatId": %s
                             }
                             """.formatted(votingOptionId, flatId);
        mockMvc.perform(post("/voting-response")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.votingOptionId").value(votingResponse.getVotingOptionId()))
               .andExpect(jsonPath("$.flatId").value(votingResponse.getFlatId()));
    }

    @Test
    void shouldDeletedVotingResponseWhenDelete() throws Exception {
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .id(1L)
                                                            .votingOptionId(1L)
                                                            .flatId(1L)
                                                            .build();

        mockMvc.perform(delete("/voting-response/{votingResponseId}", votingResponse.getId())
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndVotingNotExist() throws Exception {
        Long votingResponseId = 1L;

        doThrow(NotFoundException.class).when(votingResponseServiceMock)
                                        .delete(votingResponseId);

        mockMvc.perform(delete("/voting-response/{votingResponseId}", votingResponseId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedVotingWhenUpdate() throws Exception {
        Long id = 1L;
        Long votingOptionId = 1L;
        Long flatId = 1L;
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .id(id)
                                                            .votingOptionId(votingOptionId)
                                                            .flatId(flatId)
                                                            .build();

        when(votingResponseServiceMock.update(any())).thenReturn(votingResponse);

        String updateRequest = """
                               {
                                  "id": %s,
                                  "votingOptionId": %s,
                                  "flatId": %s
                               }
                               """.formatted(id, votingOptionId, flatId);
        mockMvc.perform(patch("/voting-response")
                                .contentType(APPLICATION_JSON)
                                .content(updateRequest))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(votingResponse.getId()))
               .andExpect(jsonPath("$.votingOptionId").value(votingResponse.getVotingOptionId()))
               .andExpect(jsonPath("$.flatId").value(votingResponse.getFlatId()));
    }

}