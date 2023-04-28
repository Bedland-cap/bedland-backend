package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.BuildingDto;
import com.capgemini.bedland.providers.BuildingProvider;
import com.capgemini.bedland.services.BuildingService;
import com.capgemini.bedland.services.CustomBuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BuildingControllerTest {

    @Mock
    private BuildingService buildingServiceMock;
    @Mock
    private BuildingProvider buildingProviderMock;

    @Mock
    private CustomBuildingService customBuildingServiceMock;
    private MockMvc mockMvc;

    @BeforeEach
    void setUpBuildingControllerTest() {
        BuildingController controller = new BuildingController(buildingServiceMock, buildingProviderMock,customBuildingServiceMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnAllBuildingsWhenGettingAllBuildings() throws Exception {
        //given
        BuildingDto buildingDto1 = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .id(1L)
                .build();
        BuildingDto buildingDto2 = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name2")
                .address("Dubai 2132")
                .floors(55)
                .id(2L)
                .build();
        //when
        when(buildingProviderMock.getAll()).thenReturn(List.of(buildingDto1, buildingDto2));
        //then
        mockMvc.perform(get("/building"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(buildingDto1.getId()))
                .andExpect(jsonPath("$[0].managerId").value(buildingDto1.getManagerId()))
                .andExpect(jsonPath("$[0].buildingName").value(buildingDto1.getBuildingName()))
                .andExpect(jsonPath("$[0].address").value(buildingDto1.getAddress()))
                .andExpect(jsonPath("$[0].floors").value(buildingDto1.getFloors()))

                .andExpect(jsonPath("$[1].id").value(buildingDto2.getId()))
                .andExpect(jsonPath("$[1].managerId").value(buildingDto2.getManagerId()))
                .andExpect(jsonPath("$[1].buildingName").value(buildingDto2.getBuildingName()))
                .andExpect(jsonPath("$[1].address").value(buildingDto2.getAddress()))
                .andExpect(jsonPath("$[1].floors").value(buildingDto2.getFloors()))

                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void shouldEmptyListWhenGettingAllBuildingsAndThereAreNone() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/building"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnBuildingWhenGettingBuildingById() throws Exception {
        //given
        long sampleId = 1L;
        BuildingDto buildingDto1 = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .id(sampleId)
                .build();

        //when
        when(buildingProviderMock.getById(buildingDto1.getId())).thenReturn(buildingDto1);
        //then
        mockMvc.perform(get("/building/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(buildingDto1.getId()))
                .andExpect(jsonPath("$.managerId").value(buildingDto1.getManagerId()))
                .andExpect(jsonPath("$.buildingName").value(buildingDto1.getBuildingName()))
                .andExpect(jsonPath("$.address").value(buildingDto1.getAddress()))
                .andExpect(jsonPath("$.floors").value(buildingDto1.getFloors()));
    }

    @Test
    void shouldReturnNoRecordWhenWhenGettingBuildingByIdAndIDIsNotInDB() throws Exception {
        //given
        long sampleId = 1L;
        //when
        //then
        mockMvc.perform(get("/building/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldCreateBuildingWhenCreatingBuilding() throws Exception {
        //given
        BuildingDto buildingDto1 = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .build();

        String requestedBody = """
                {
                      "managerId": "%s",
                      "buildingName": "%s",
                      "address": "%s",
                      "floors": "%s"
                }
                """.formatted(buildingDto1.getManagerId(),
                buildingDto1.getBuildingName(),
                buildingDto1.getAddress(),
                buildingDto1.getFloors());
        //when
        when(buildingServiceMock.create(buildingDto1)).thenReturn(buildingDto1);
        //then
        mockMvc.perform(post("/building").contentType(MediaType.APPLICATION_JSON)
                        .content(requestedBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(buildingDto1.getId()))
                .andExpect(jsonPath("$.managerId").value(buildingDto1.getManagerId()))
                .andExpect(jsonPath("$.buildingName").value(buildingDto1.getBuildingName()))
                .andExpect(jsonPath("$.address").value(buildingDto1.getAddress()))
                .andExpect(jsonPath("$.floors").value(buildingDto1.getFloors()));
    }

    @Test
    void shouldUpdateBuildingWhenCreatingBuilding() throws Exception {
        //given
        BuildingDto buildingDto1 = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .build();

        String requestedBody = """
                {
                      "managerId": "%s",
                      "buildingName": "%s",
                      "address": "%s",
                      "floors": "%s"
                }
                """.formatted(buildingDto1.getManagerId(),
                buildingDto1.getBuildingName(),
                buildingDto1.getAddress(),
                buildingDto1.getFloors());
        //when
        when(buildingServiceMock.update(buildingDto1)).thenReturn(buildingDto1);
        //then
        mockMvc.perform(patch("/building").contentType(MediaType.APPLICATION_JSON)
                        .content(requestedBody))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(buildingDto1.getId()))
                .andExpect(jsonPath("$.managerId").value(buildingDto1.getManagerId()))
                .andExpect(jsonPath("$.buildingName").value(buildingDto1.getBuildingName()))
                .andExpect(jsonPath("$.address").value(buildingDto1.getAddress()))
                .andExpect(jsonPath("$.floors").value(buildingDto1.getFloors()));
    }

    @Test
    void shouldDeleteBuildingWhenDeletingBuilding() throws Exception {
        //given
        long sampleId = 1000L;
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/building/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnPhotoWhenGetPhotoByBuildingId() throws Exception {
        //given
        long sampleId = 1L;
        byte[] data = new byte[2];
        BuildingDto buildingDto = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .id(sampleId)
                .photo(data)
                .build();

        //when
        when(buildingProviderMock.getPhotoByBuildingId(buildingDto.getId())).thenReturn(data);
        //then
        mockMvc.perform(get("/building/image/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldUpdatePhotoWhenUpdatingBuildingPhoto() throws Exception {
        //given
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        MockMultipartFile file = new MockMultipartFile("name.xD", data);
        long id = 1L;
        BuildingDto updateBuilding = BuildingDto.builder()
                .managerId(1L)
                .buildingName("test name1")
                .address("Dubai 2133")
                .floors(55)
                .id(id)
                .photo(data)
                .build();

        //when
        when(buildingServiceMock.updatePhoto(any(), any())).thenReturn(updateBuilding);
        //then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/building/image/{id}", updateBuilding.getId())
                        .param("image",
                                Arrays.toString(
                                        file.getBytes())))

                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateBuilding.getId()))
                .andExpect(jsonPath("$.managerId").value(updateBuilding.getManagerId()))
                .andExpect(jsonPath("$.buildingName").value(updateBuilding.getBuildingName()))
                .andExpect(jsonPath("$.address").value(updateBuilding.getAddress()))
                .andExpect(jsonPath("$.floors").value(updateBuilding.getFloors()))
                .andExpect(jsonPath("$.photo").isNotEmpty());
    }

}
