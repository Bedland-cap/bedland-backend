package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.controllers.VotingOptionController;
import com.capgemini.bedland.dtos.VotingOptionDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.VotingOptionProvider;
import com.capgemini.bedland.services.VotingOptionService;
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
class VotingOptionControllerTest {

    private MockMvc mockMvc;
    @Mock
    private VotingOptionService votingOptionServiceMock;
    @Mock
    private VotingOptionProvider votingOptionProviderMock;

    @BeforeEach
    void setUpController() {
        VotingOptionController controller = new VotingOptionController(votingOptionProviderMock,
                                                                       votingOptionServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnListOfVotingOptionWhenGetAll() throws Exception {
        VotingOptionDto votingOption1 = VotingOptionDto.builder()
                                                       .votingId(1L)
                                                       .title("Title")
                                                       .build();
        VotingOptionDto votingOption2 = VotingOptionDto.builder()
                                                       .votingId(1L)
                                                       .title("Title")
                                                       .build();

        when(votingOptionProviderMock.getAll()).thenReturn(List.of(votingOption1, votingOption2));

        mockMvc.perform((get("/voting-option").contentType(APPLICATION_JSON)))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].votingId").value(votingOption1.getVotingId()))
               .andExpect(jsonPath("$[0].title").value(votingOption1.getTitle()))
               .andExpect(jsonPath("$[1].votingId").value(votingOption2.getVotingId()))
               .andExpect(jsonPath("$[1].title").value(votingOption2.getTitle()))
               .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllVotingOptionAndZeroVotingOptionExist() throws Exception {
        mockMvc.perform(get("/voting-option").contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnVotingOptionWhenGetVotingOptionById() throws Exception {
        Long votingOptionId = 1L;
        VotingOptionDto votingOption = VotingOptionDto.builder()
                                                      .id(votingOptionId)
                                                      .votingId(1L)
                                                      .title("Title")
                                                      .build();

        when(votingOptionProviderMock.getById(votingOptionId)).thenReturn(votingOption);

        mockMvc.perform(get("/voting-option/{votingOptionId}", votingOptionId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.votingId").value(votingOption.getVotingId()))
               .andExpect(jsonPath("$.title").value(votingOption.getTitle()));
    }

    @Test
    void shouldReturnNotFoundWhenGetVotingOptionByIdAndVotingOptionNoExist() throws Exception {
        Long votingOptionId = 1L;

        doThrow(NotFoundException.class).when(votingOptionProviderMock)
                                        .getById(votingOptionId);

        mockMvc.perform(get("/voting-option/{votingOptionId}", votingOptionId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCreatedVotingOptionWhenCreate() throws Exception {
        Long votingId = 1L;
        String title = "title";
        VotingOptionDto votingOption = VotingOptionDto.builder()
                                                      .votingId(votingId)
                                                      .title(title)
                                                      .build();
        when(votingOptionServiceMock.create(any())).thenReturn(votingOption);

        String requestBody = """
                             {
                                "votingId": %s,
                                "title": "%s"                     
                             }
                             """.formatted(votingId, title);
        mockMvc.perform(post("/voting-option")
                                .contentType(APPLICATION_JSON)
                                .content(requestBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.votingId").value(votingOption.getVotingId()))
               .andExpect(jsonPath("$.title").value(votingOption.getTitle()));
    }

    @Test
    void shouldDeletedVotingOptionWhenDelete() throws Exception {
        VotingOptionDto votingOption = VotingOptionDto.builder()
                                                      .id(1L)
                                                      .votingId(1L)
                                                      .title("Title")
                                                      .build();

        mockMvc.perform(delete("/voting-option/{votingOptionId}", votingOption.getId())
                                .contentType(MediaType.APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnStatusNotFoundWhenDeleteAndVotingOptionNotExist() throws Exception {
        Long votingOptionId = 1L;

        doThrow(NotFoundException.class).when(votingOptionServiceMock)
                                        .delete(votingOptionId);

        mockMvc.perform(delete("/voting-option/{votingOptionId}", votingOptionId)
                                .contentType(APPLICATION_JSON))
               .andDo(log())
               .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedVotingWhenUpdate() throws Exception {
        Long id = 1L;
        Long votingId = 1L;
        String title = "title";
        VotingOptionDto votingOption = VotingOptionDto.builder()
                                                      .id(id)
                                                      .votingId(votingId)
                                                      .title(title)
                                                      .build();

        when(votingOptionServiceMock.update(any())).thenReturn(votingOption);

        String updateRequest = """
                               {
                                  "id": %s,
                                  "votingId": %s,
                                  "title": "%s"                     
                               } 
                               """.formatted(id, votingId, title);
        mockMvc.perform(patch("/voting-option")
                                .contentType(APPLICATION_JSON)
                                .content(updateRequest))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.votingId").value(votingOption.getVotingId()))
               .andExpect(jsonPath("$.title").value(votingOption.getTitle()));
    }

}