package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.voting.internal.custom.CustomVotingController;
import com.capgemini.bedland.voting.internal.custom.CustomVotingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
 class CustomVotingControllerTest {

    private MockMvc mockMvc;
    @Mock
    private CustomVotingService customVotingServiceMock;

    @BeforeEach
    void setUpController() {
        CustomVotingController controller = new CustomVotingController(customVotingServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnListOfVotingsForGivenManagerWhenFindingVotingsForGivenManager() throws Exception {
        //given
        Long managerId = 1L;

        VotingDto voting1 = VotingDto.builder().buildingId(1L).expirationDate(LocalDateTime.now()).title("Title1").description("Description1").build();
        VotingDto voting2 = VotingDto.builder().expirationDate(LocalDateTime.now()).buildingId(3L).title("Title2").description("Description2").build();
        VotingDto voting3 = VotingDto.builder().expirationDate(LocalDateTime.now()).buildingId(2L).title("Title2").description("Description2").build();
        //when
        when(customVotingServiceMock.findAllVotingsForGivenManager(managerId)).thenReturn(List.of(voting1, voting2, voting3));
        //then
        mockMvc.perform(get("/voting_summary").param("manager_id", String.valueOf(managerId))).andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].buildingId").value(voting1.getBuildingId()))
                .andExpect(jsonPath("$[0].title").value(voting1.getTitle()))
                .andExpect(jsonPath("$[0].description").value(voting1.getDescription()))
                .andExpect(jsonPath("$[1].buildingId").value(voting2.getBuildingId()))
                .andExpect(jsonPath("$[1].title").value(voting2.getTitle()))
                .andExpect(jsonPath("$[1].description").value(voting2.getDescription()))
                .andExpect(jsonPath("$[2].buildingId").value(voting3.getBuildingId()))
                .andExpect(jsonPath("$[2].title").value(voting3.getTitle()))
                .andExpect(jsonPath("$[2].description").value(voting3.getDescription()))
                .andExpect(jsonPath("$[3]").doesNotExist());
    }

    @Test
    void shouldReturnNotFoundExceptionWhenFiningVotingsForGivenManagerAndManagerIdIsNotInDB() throws Exception {
        //given
        Long managerId = 1L;
        //when
        doThrow(NotFoundException.class).when(customVotingServiceMock)
                .findAllVotingsForGivenManager(managerId);
        //then
        mockMvc.perform(get("/voting_summary").param("manager_id", String.valueOf(managerId))).andDo(log())
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnEmptyListWhenFindingVotingsForGivenManagerAndThereAreNoneVotingsRelated() throws Exception {
        //given
        Long managerId = 1L;
        //when
        //then
        mockMvc.perform(get("/voting_summary").param("manager_id", String.valueOf(managerId))).andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }
}
