package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.MemberDto;
import com.capgemini.bedland.providers.MemberProvider;
import com.capgemini.bedland.services.MemberService;
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
class MemberControllerTest {

    @Mock
    private MemberService memberServiceMock;

    @Mock
    private MemberProvider memberProviderMock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpAnnouncementControllerTest() {
        MemberController controller = new MemberController(memberServiceMock, memberProviderMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }

    @Test
    void shouldReturnAllMembersWhenGettingAllMembers() throws Exception {
        //given
        MemberDto firstSampleMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(1L)
                .build();
        MemberDto secondSampleMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(2L)
                .build();

        //when
        when(memberProviderMock.getAll()).thenReturn(List.of(firstSampleMember, secondSampleMember));
        //then
        mockMvc.perform(get("/member"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(firstSampleMember.getId()))
                .andExpect(jsonPath("$[0].name").value(firstSampleMember.getName()))
                .andExpect(jsonPath("$[0].lastName").value(firstSampleMember.getLastName()))
                .andExpect(jsonPath("$[0].email").value(firstSampleMember.getEmail()))
                .andExpect(jsonPath("$[0].phoneNumber").value(firstSampleMember.getPhoneNumber()))

                .andExpect(jsonPath("$[1].id").value(secondSampleMember.getId()))
                .andExpect(jsonPath("$[1].name").value(secondSampleMember.getName()))
                .andExpect(jsonPath("$[1].lastName").value(secondSampleMember.getLastName()))
                .andExpect(jsonPath("$[1].email").value(secondSampleMember.getEmail()))
                .andExpect(jsonPath("$[1].phoneNumber").value(secondSampleMember.getPhoneNumber()))

                .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void shouldEmptyListWhenGettingAllMembersAndThereAreNone() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/member"))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnMemberWhenGettingMemberById() throws Exception {
        //given
        long sampleId = 1L;
        MemberDto firstSampleMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(1L)
                .build();
        //when
        when(memberProviderMock.getById(firstSampleMember.getId())).thenReturn(firstSampleMember);
        //then
        mockMvc.perform(get("/member/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstSampleMember.getId()))
                .andExpect(jsonPath("$.name").value(firstSampleMember.getName()))
                .andExpect(jsonPath("$.lastName").value(firstSampleMember.getLastName()))
                .andExpect(jsonPath("$.email").value(firstSampleMember.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(firstSampleMember.getPhoneNumber()));
    }

    @Test
    void shouldReturnNoRecordWhenWhenGettingMemberByIdAndIDIsNotInDB() throws Exception {
        //given
        long sampleId = 1L;
        //when
        //then
        mockMvc.perform(get("/member/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldCreateMemberWhenCreatingMember() throws Exception {
        //given
        MemberDto creationRequest = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(1L)
                .build();

        String requestedBody = """
                {                  
                      "name": "%s",
                      "lastName": "%s",
                      "email": "%s",
                      "phoneNumber": "%s",
                      "flatId": "%s"
                }
                """.formatted(creationRequest.getName(),
                creationRequest.getLastName(),
                creationRequest.getEmail(),
                creationRequest.getPhoneNumber(),
                creationRequest.getFlatId());
        //when
        when(memberServiceMock.create(creationRequest)).thenReturn(creationRequest);
        //then
        mockMvc.perform(post("/member").contentType(MediaType.APPLICATION_JSON)
                        .content(requestedBody))
                .andDo(log())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(creationRequest.getId()))
                .andExpect(jsonPath("$.name").value(creationRequest.getName()))
                .andExpect(jsonPath("$.lastName").value(creationRequest.getLastName()))
                .andExpect(jsonPath("$.email").value(creationRequest.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(creationRequest.getPhoneNumber()));
    }

    @Test
    void shouldUpdateMemberWhenUpdatingMember() throws Exception {
        //given
        MemberDto updateRequest = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(1L)
                .build();

        String requestedBody = """
                {
                      "name": "%s",
                      "lastName": "%s",
                      "email": "%s",
                      "phoneNumber": "%s",
                      "flatId": "%s"
                }
                """.formatted(updateRequest.getName(),
                updateRequest.getLastName(),
                updateRequest.getEmail(),
                updateRequest.getPhoneNumber(),
                updateRequest.getFlatId());
        //when
        when(memberServiceMock.update(updateRequest)).thenReturn(updateRequest);
        //then
        mockMvc.perform(MockMvcRequestBuilders.patch("/member")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestedBody))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updateRequest.getId()))
                .andExpect(jsonPath("$.name").value(updateRequest.getName()))
                .andExpect(jsonPath("$.lastName").value(updateRequest.getLastName()))
                .andExpect(jsonPath("$.email").value(updateRequest.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(updateRequest.getPhoneNumber()));
    }

    @Test
    void shouldDeleteMemberWhenDeletingMember() throws Exception {
        //given
        long sampleId = 1000L;
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/member/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAvatarWhenGetAvatarByMemberId() throws Exception {
        //given
        long sampleId = 1L;
        byte[] data = new byte[2];
        MemberDto sampleMember = MemberDto.builder()
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .id(1L)
                .build();

        //when
        when(memberProviderMock.getAvatarByMemberId(sampleMember.getId())).thenReturn(data);
        //then
        mockMvc.perform(get("/member/image/{id}", sampleId))
                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldUpdateAvatarWhenUpdatingManagerAvatar() throws Exception {
        //given
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        MockMultipartFile file = new MockMultipartFile("name.xD", data);
        long id = 1L;
        MemberDto updatedMember = MemberDto.builder()
                .id(id)
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .flatId(1L)
                .avatar(data)
                .build();

        //when
        when(memberServiceMock.updateAvatar(any(), any())).thenReturn(updatedMember);
        //then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/member/image/{id}", updatedMember.getId())
                        .param("image",
                                Arrays.toString(
                                        file.getBytes())))

                .andDo(log())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedMember.getId()))
                .andExpect(jsonPath("$.name").value(updatedMember.getName()))
                .andExpect(jsonPath("$.lastName").value(updatedMember.getLastName()))
                .andExpect(jsonPath("$.email").value(updatedMember.getEmail()))
                .andExpect(jsonPath("$.phoneNumber").value(updatedMember.getPhoneNumber()))
                .andExpect(jsonPath("$.avatar").isNotEmpty());
    }

}
