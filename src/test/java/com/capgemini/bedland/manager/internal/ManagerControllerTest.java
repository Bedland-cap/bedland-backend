package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.manager.api.ManagerProvider;
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
class ManagerControllerTest {

    @Mock
    private ManagerService managerServiceMock;

    @Mock
    private ManagerProvider managerProviderMock;

    private MockMvc mockMvc;

    @BeforeEach
    void setUpAnnouncementControllerTest() {
        ManagerController controller = new ManagerController(managerServiceMock, managerProviderMock);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                                 .build();
    }

    @Test
    void shouldReturnAllManagersWhenGettingAllMembers() throws Exception {
        //given
        ManagerDto firstSampleManager = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .id(1L)
                .build();

        ManagerDto secondSampleManager = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .id(1L)
                .build();

        //when
        when(managerProviderMock.getAll()).thenReturn(List.of(firstSampleManager, secondSampleManager));
        //then
        mockMvc.perform(get("/manager"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray())
               .andExpect(jsonPath("$[0].id").value(firstSampleManager.getId()))
               .andExpect(jsonPath("$[0].login").value(firstSampleManager.getLogin()))
               .andExpect(jsonPath("$[0].password").value(firstSampleManager.getPassword()))
               .andExpect(jsonPath("$[0].name").value(firstSampleManager.getName()))
               .andExpect(jsonPath("$[0].lastName").value(firstSampleManager.getLastName()))
               .andExpect(jsonPath("$[0].email").value(firstSampleManager.getEmail()))
               .andExpect(jsonPath("$[0].phoneNumber").value(firstSampleManager.getPhoneNumber()))

               .andExpect(jsonPath("$[1].id").value(secondSampleManager.getId()))
               .andExpect(jsonPath("$[1].login").value(secondSampleManager.getLogin()))
               .andExpect(jsonPath("$[1].password").value(secondSampleManager.getPassword()))
               .andExpect(jsonPath("$[1].name").value(secondSampleManager.getName()))
               .andExpect(jsonPath("$[1].lastName").value(secondSampleManager.getLastName()))
               .andExpect(jsonPath("$[1].email").value(secondSampleManager.getEmail()))
               .andExpect(jsonPath("$[1].phoneNumber").value(secondSampleManager.getPhoneNumber()))

               .andExpect(jsonPath("$[2].id").doesNotExist());
    }

    @Test
    void shouldEmptyListWhenGettingAllManagersAndThereAreNone() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(get("/manager"))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnManagerWhenGettingManagerById() throws Exception {
        //given
        long sampleId = 1L;
        ManagerDto firstSampleManager = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .id(1L)
                .build();
        //when
        when(managerProviderMock.getById(firstSampleManager.getId())).thenReturn(firstSampleManager);
        //then
        mockMvc.perform(get("/manager/{id}", sampleId))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(firstSampleManager.getId()))
               .andExpect(jsonPath("$.login").value(firstSampleManager.getLogin()))
               .andExpect(jsonPath("$.password").value(firstSampleManager.getPassword()))
               .andExpect(jsonPath("$.name").value(firstSampleManager.getName()))
               .andExpect(jsonPath("$.lastName").value(firstSampleManager.getLastName()))
               .andExpect(jsonPath("$.email").value(firstSampleManager.getEmail()))
               .andExpect(jsonPath("$.phoneNumber").value(firstSampleManager.getPhoneNumber()));
    }

    @Test
    void shouldReturnNoRecordWhenWhenGettingManagerByIdAndIDIsNotInDB() throws Exception {
        //given
        long sampleId = 1L;
        //when
        //then
        mockMvc.perform(get("/manager/{id}", sampleId))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").doesNotExist());
    }

    @Test
    void shouldReturnAvatarWhenGetAvatarByManagerId() throws Exception {
        //given
        long sampleId = 1L;
        byte[] data = new byte[2];
        ManagerDto sampleManager = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .id(1L)
                .avatar(data)
                .build();
        //when
        when(managerProviderMock.getAvatarByManagerId(sampleManager.getId())).thenReturn(data);
        //then
        mockMvc.perform(get("/manager/image/{id}", sampleId))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").exists());
    }

    @Test
    void shouldCreateManagerWhenCreatingManager() throws Exception {
        //given
        ManagerDto creationRequest = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .build();

        String requestedBody = """
                               {
                                     "login": "%s",
                                     "password": "%s",
                                     "name": "%s",
                                     "lastName": "%s",
                                     "email": "%s",
                                     "phoneNumber": "%s"
                               }
                               """.formatted(creationRequest.getLogin(),
                                             creationRequest.getPassword(),
                                             creationRequest.getName(),
                                             creationRequest.getLastName(),
                                             creationRequest.getEmail(),
                                             creationRequest.getPhoneNumber());
        //when
        when(managerServiceMock.create(creationRequest)).thenReturn(creationRequest);
        //then
        mockMvc.perform(post("/manager").contentType(MediaType.APPLICATION_JSON)
                                        .content(requestedBody))
               .andDo(log())
               .andExpect(status().isCreated())
               .andExpect(jsonPath("$.id").value(creationRequest.getId()))
               .andExpect(jsonPath("$.login").value(creationRequest.getLogin()))
               .andExpect(jsonPath("$.password").value(creationRequest.getPassword()))
               .andExpect(jsonPath("$.name").value(creationRequest.getName()))
               .andExpect(jsonPath("$.lastName").value(creationRequest.getLastName()))
               .andExpect(jsonPath("$.email").value(creationRequest.getEmail()))
               .andExpect(jsonPath("$.phoneNumber").value(creationRequest.getPhoneNumber()));
    }

    @Test
    void shouldUpdateManagerWhenUpdatingManager() throws Exception {
        //given
        ManagerDto creationRequest = ManagerDto
                .builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .build();

        String requestedBody = """
                               {
                                     "login": "%s",
                                     "password": "%s",
                                     "name": "%s",
                                     "lastName": "%s",
                                     "email": "%s",
                                     "phoneNumber": "%s"
                               }
                               """.formatted(creationRequest.getLogin(),
                                             creationRequest.getPassword(),
                                             creationRequest.getName(),
                                             creationRequest.getLastName(),
                                             creationRequest.getEmail(),
                                             creationRequest.getPhoneNumber());
        //when
        when(managerServiceMock.update(creationRequest)).thenReturn(creationRequest);
        //then
        mockMvc.perform(patch("/manager").contentType(MediaType.APPLICATION_JSON)
                                         .content(requestedBody))
               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(creationRequest.getId()))
               .andExpect(jsonPath("$.login").value(creationRequest.getLogin()))
               .andExpect(jsonPath("$.password").value(creationRequest.getPassword()))
               .andExpect(jsonPath("$.name").value(creationRequest.getName()))
               .andExpect(jsonPath("$.lastName").value(creationRequest.getLastName()))
               .andExpect(jsonPath("$.email").value(creationRequest.getEmail()))
               .andExpect(jsonPath("$.phoneNumber").value(creationRequest.getPhoneNumber()));
    }

    @Test
    void shouldUpdateAvatarWhenUpdatingManagerAvatar() throws Exception {
        //given
        byte[] data = {1, 2, 3, 4, 5, 6, 7, 8};
        MockMultipartFile file = new MockMultipartFile("name.xD", data);
        long id = 1L;
        ManagerDto updatedManager = ManagerDto
                .builder()
                .id(id)
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .avatar(data)
                .build();

        //when
        when(managerServiceMock.updateAvatar(any(), any())).thenReturn(updatedManager);
        //then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/manager/image/{id}", updatedManager.getId())
                                .param("image",
                                       Arrays.toString(
                                               file.getBytes())))

               .andDo(log())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(updatedManager.getId()))
               .andExpect(jsonPath("$.login").value(updatedManager.getLogin()))
               .andExpect(jsonPath("$.password").value(updatedManager.getPassword()))
               .andExpect(jsonPath("$.name").value(updatedManager.getName()))
               .andExpect(jsonPath("$.lastName").value(updatedManager.getLastName()))
               .andExpect(jsonPath("$.email").value(updatedManager.getEmail()))
               .andExpect(jsonPath("$.phoneNumber").value(updatedManager.getPhoneNumber()))
               .andExpect(jsonPath("$.phoneNumber").value(updatedManager.getPhoneNumber()))
               .andExpect(jsonPath("$.avatar").isNotEmpty());
    }

    @Test
    void shouldDeleteMemberWhenDeletingMember() throws Exception {
        //given
        long sampleId = 1000L;
        //when
        //then
        mockMvc.perform(MockMvcRequestBuilders.delete("/manager/{id}", sampleId))
               .andDo(log())
               .andExpect(status().isNoContent());
    }

}
