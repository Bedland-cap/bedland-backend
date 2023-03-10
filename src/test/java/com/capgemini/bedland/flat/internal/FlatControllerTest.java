package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.building.internal.BuildingDto;
import com.capgemini.bedland.flat.api.FlatProvider;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class FlatControllerTest {

    @Mock
    private FlatService flatServiceMock;

    @Mock
    private FlatProvider flatProviderMock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpFlatControllerTest() {
        FlatController controller = new FlatController(flatServiceMock, flatProviderMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void shouldReturnAllFlatsWhenGettingAllFlats() throws Exception {
        //given
        FlatDto flatDto1 = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").id(1L).build();
        FlatDto flatDto2 = FlatDto.builder().buildingId(1L).number("5").floor(1).shapePath("dsdf23413").id(2L).build();

        //when
        when(flatProviderMock.getAll()).thenReturn(List.of(flatDto1, flatDto2));
        //then
        mockMvc.perform(get("/flat")).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(flatDto1.getId()))
                .andExpect(jsonPath("$[0].buildingId").value(flatDto1.getBuildingId()))
                .andExpect(jsonPath("$[0].number").value(flatDto1.getNumber()))
                .andExpect(jsonPath("$[0].floor").value(flatDto1.getFloor()))
                .andExpect(jsonPath("$[0].shapePath").value(flatDto1.getShapePath()))

                .andExpect(jsonPath("$[1].id").value(flatDto2.getId()))
                .andExpect(jsonPath("$[1].buildingId").value(flatDto2.getBuildingId()))
                .andExpect(jsonPath("$[1].number").value(flatDto2.getNumber()))
                .andExpect(jsonPath("$[1].floor").value(flatDto2.getFloor()))
                .andExpect(jsonPath("$[1].shapePath").value(flatDto2.getShapePath()))

                .andExpect(jsonPath("$[2].id").doesNotExist());
    }
    @Test
    void shouldEmptyListWhenGettingAllFlatsAndThereAreNone() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/flat")).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnFlatWhenGettingFlatById() throws Exception {
        //given
        long sampleId = 1L;
        FlatDto flatDto1 = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").id(1L).build();

        //when
        when(flatProviderMock.getById(flatDto1.getId())).thenReturn(flatDto1);
        //then
        mockMvc.perform(get("/flat/{id}", sampleId)).andDo(log())
                  .andExpect(jsonPath("$.id").value(flatDto1.getId()))
                .andExpect(jsonPath("$.buildingId").value(flatDto1.getBuildingId()))
                .andExpect(jsonPath("$.number").value(flatDto1.getNumber()))
                .andExpect(jsonPath("$.floor").value(flatDto1.getFloor()))
                .andExpect(jsonPath("$.shapePath").value(flatDto1.getShapePath()));
    }

    @Test
    void shouldReturnNoRecordWhenWhenGettingFlatByIdAndIDIsNotInDB() throws Exception {
        //given
        long sampleId = 1L;
        //when
        //then
        mockMvc.perform(get("/flat/{id}", sampleId)).andDo(log()).andExpect(status().isOk()).andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldCreateFlatWhenCreatingFlat() throws Exception {
        //given
        FlatDto flatDto1 = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").id(1L).build();

        String requestedBody = """
                {
                      "buildingId": "%s",
                      "number": "%s",
                      "floor": "%s",
                      "shapePath": "%s"
                }
                """.formatted(flatDto1.getBuildingId(),flatDto1.getNumber(),flatDto1.getFloor(),flatDto1.getShapePath());
        //when
        when(flatServiceMock.create(flatDto1)).thenReturn(flatDto1);
        //then
        mockMvc.perform(post("/flat").contentType(MediaType.APPLICATION_JSON).content(requestedBody)).andDo(log()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(flatDto1.getId()))
                .andExpect(jsonPath("$.buildingId").value(flatDto1.getBuildingId()))
                .andExpect(jsonPath("$.number").value(flatDto1.getNumber()))
                .andExpect(jsonPath("$.floor").value(flatDto1.getFloor()))
                .andExpect(jsonPath("$.shapePath").value(flatDto1.getShapePath()));
    }

    @Test
    void shouldUpdateFlatWhenCreatingFlat() throws Exception {
        //given
        FlatDto flatDto1 = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").id(1L).build();


        String requestedBody = """
                {
                      "buildingId": "%s",
                      "number": "%s",
                      "floor": "%s",
                      "shapePath": "%s"
                }
                """.formatted(flatDto1.getBuildingId(),flatDto1.getNumber(),flatDto1.getFloor(),flatDto1.getShapePath());
        //when
        when(flatServiceMock.update(flatDto1)).thenReturn(flatDto1);
        //then
        mockMvc.perform(patch("/flat").contentType(MediaType.APPLICATION_JSON).content(requestedBody)).andDo(log()).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(flatDto1.getId()))
                .andExpect(jsonPath("$.buildingId").value(flatDto1.getBuildingId()))
                .andExpect(jsonPath("$.number").value(flatDto1.getNumber()))
                .andExpect(jsonPath("$.floor").value(flatDto1.getFloor()))
                .andExpect(jsonPath("$.shapePath").value(flatDto1.getShapePath()));
    }

    @Test
    void shouldDeleteFlatWhenDeletingFlat() throws Exception {
        //given
        long sampleId = 1000L;
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/flat/{id}", sampleId)).andDo(log()).andExpect(status().isNoContent());
    }
}
