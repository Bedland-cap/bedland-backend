package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.announcement.api.AnnouncementProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AnnouncementControllerTest {

    @Mock
    private AnnouncementProvider announcementProviderMock;
    @Mock
    private AnnouncementService announcementServiceMock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpAnnouncementControllerTest() {
        AnnouncementController controller = new AnnouncementController(announcementServiceMock, announcementProviderMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnAllAnnouncementsWhenGettingAllAnnouncements() throws Exception {
        //given
        AnnouncementDto firstMockedAnnouncement = AnnouncementDto.builder().title("mock announcement 1").id(1L).buildingId(1L).description("Some random description1").flatId(1L).build();
        AnnouncementDto secondMockedAnnouncement = AnnouncementDto.builder().title("mock announcement 2").id(2L).buildingId(1L).description("Some random description2").flatId(1L).build();
        //when
        when(announcementProviderMock.getAll()).thenReturn(List.of(firstMockedAnnouncement, secondMockedAnnouncement));
        //then
        mockMvc.perform(get("/announcement")).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0].id").value(firstMockedAnnouncement.getId())).andExpect(jsonPath("$[0].title").value(firstMockedAnnouncement.getTitle())).andExpect(jsonPath("$[0].buildingId").value(firstMockedAnnouncement.getBuildingId())).andExpect(jsonPath("$[0].description").value(firstMockedAnnouncement.getDescription())).andExpect(jsonPath("$[0].flatId").value(firstMockedAnnouncement.getFlatId())).andExpect(jsonPath("$[1].id").value(secondMockedAnnouncement.getId())).andExpect(jsonPath("$[1].title").value(secondMockedAnnouncement.getTitle())).andExpect(jsonPath("$[1].buildingId").value(secondMockedAnnouncement.getBuildingId())).andExpect(jsonPath("$[1].description").value(secondMockedAnnouncement.getDescription())).andExpect(jsonPath("$[1].flatId").value(secondMockedAnnouncement.getFlatId())).andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void shouldReturnEmptyListWhenGettingAllAnnouncementsAndThereAreNone() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/announcement")).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$").isArray()).andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnAnnouncementWhenGettingAnnouncementById() throws Exception {
        //given
        long sampleId = 1L;
        AnnouncementDto firstMockedAnnouncement = AnnouncementDto.builder().title("mock announcement 1").id(sampleId).buildingId(1L).description("Some random description1").flatId(1L).build();
        AnnouncementDto secondMockedAnnouncement = AnnouncementDto.builder().title("mock announcement 2").id(sampleId + 1).buildingId(1L).description("Some random description2").flatId(1L).build();
        //when
        when(announcementProviderMock.getById(firstMockedAnnouncement.getId())).thenReturn(firstMockedAnnouncement);
        //then
        mockMvc.perform(get("/announcement/{id}", sampleId)).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(firstMockedAnnouncement.getId())).andExpect(jsonPath("$.title").value(firstMockedAnnouncement.getTitle())).andExpect(jsonPath("$.buildingId").value(firstMockedAnnouncement.getBuildingId())).andExpect(jsonPath("$.description").value(firstMockedAnnouncement.getDescription())).andExpect(jsonPath("$.flatId").value(firstMockedAnnouncement.getFlatId())).andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void shouldReturnNoRecordWhenGettingAnnouncementByIdAndIDIsNotInDB() throws Exception {
        //given
        long sampleId = 1L;
        //when
        //then
        mockMvc.perform(get("/announcement/{id}", sampleId)).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldCreateAnnouncementWhenCreatingAnnouncement() throws Exception {
        //given
        AnnouncementDto creationRequest = AnnouncementDto.builder().title("mock announcement 1").id(1L).buildingId(1L).description("Some random description1").flatId(1L).build();

        String requestedBody = """
                {
                        "flatId": "%s",
                        "buildingId": "%s",
                        "title": "%s",
                        "description": "%s"
                }
                """.formatted(creationRequest.getFlatId(), creationRequest.getBuildingId(), creationRequest.getTitle(), creationRequest.getDescription());
        ;
        //when
        when(announcementServiceMock.create(creationRequest)).thenReturn(creationRequest);
        //then
        mockMvc.perform(post("/announcement").contentType(MediaType.APPLICATION_JSON).content(requestedBody)).andDo(log()).andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(creationRequest.getId())).andExpect(jsonPath("$.title").value(creationRequest.getTitle())).andExpect(jsonPath("$.buildingId").value(creationRequest.getBuildingId())).andExpect(jsonPath("$.description").value(creationRequest.getDescription())).andExpect(jsonPath("$.flatId").value(creationRequest.getFlatId())).andExpect(jsonPath("$[1]").doesNotExist());

    }

    @Test
    void shouldUpdateAnnouncementWhenUpdatingAnnouncement() throws Exception {
        //given
        AnnouncementDto updateRequest = AnnouncementDto.builder().title("mock announcement 1").id(1L).buildingId(1L).description("Some random description1").flatId(1L).build();

        String requestedBody = """
                {
                        "flatId": "%s",
                        "buildingId": "%s",
                        "title": "%s",
                        "description": "%s"
                }
                """.formatted(updateRequest.getFlatId(), updateRequest.getBuildingId(), updateRequest.getTitle(), updateRequest.getDescription());
        ;
        //when
        when(announcementServiceMock.update(updateRequest)).thenReturn(updateRequest);
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/announcement").contentType(MediaType.APPLICATION_JSON).content(requestedBody)).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(updateRequest.getId())).andExpect(jsonPath("$.title").value(updateRequest.getTitle())).andExpect(jsonPath("$.buildingId").value(updateRequest.getBuildingId())).andExpect(jsonPath("$.description").value(updateRequest.getDescription())).andExpect(jsonPath("$.flatId").value(updateRequest.getFlatId())).andExpect(jsonPath("$[1]").doesNotExist());

    }

    @Test
    void shouldDeleteAnnouncementWhenDeletingAnnouncement() throws Exception {
        //given
        long sampleId = 1000L;
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/announcement/{id}", sampleId)).andDo(log()).andExpect(status().isNoContent());
    }
}
