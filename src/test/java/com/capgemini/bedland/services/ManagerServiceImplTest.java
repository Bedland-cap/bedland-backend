package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.ManagerDto;
import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.ManagerMapper;
import com.capgemini.bedland.providers.ManagerProvider;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.utilities.ImageUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
class ManagerServiceImplTest {

    @Autowired
    private ManagerProvider managerProvider;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

    @AfterEach
    void cleanUp() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnAllManagersWhenGettingAllManagers() {
        //given
        List<ManagerDto> managersInDB = managerMapper.entities2DTOs(managerRepository.findAll());
        //when
        List<ManagerDto> managersReceivedWhenGettingALl = managerProvider.getAll();
        //then
        assertNotEquals(0, managersInDB.size());
        assertEquals(managersInDB.size(), managersReceivedWhenGettingALl.size());
        assertEquals(managersInDB, managersReceivedWhenGettingALl);
    }

    @Test
    void shouldFindManagerByIdWhenGettingManagerById() {
        //given
        ManagerDto expectedManager = managerMapper.entity2Dto(managerRepository.findAll()
                .get(0));
        //when
        ManagerDto foundManager = managerProvider.getById(expectedManager.getId());
        //then
        assertNotEquals(null, expectedManager.getId());
        assertNotEquals(null, foundManager);
        assertEquals(expectedManager, foundManager);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingManagerByIdAndManagerIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> managerProvider.getById(sampleID));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingManagerByIdAndManagerAndGivenIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> managerProvider.getById(sampleID));
    }

    @Test
    void shouldFindManagerAvatarByManagerIdWhenGettingManagerById() throws IOException {
        //given
        ManagerEntity manager = managerRepository.findAll()
                .get(0);
        byte[] data = {1, 2};
        MultipartFile file = new MockMultipartFile("name.xD", data);
        //when
        mockStatic(ImageUtil.class);
        when(ImageUtil.decompressImage(data)).thenReturn(data);
        manager.setAvatar(file.getBytes());
        managerRepository.save(manager);
        //then
        assertEquals(managerProvider.getAvatarByManagerId(manager.getId()), manager.getAvatar());
    }

    @Test
    void shouldCreateManagerWhenCreatingManager() {
        //given
        ManagerDto newManager = ManagerDto.builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .build();
        List<ManagerEntity> managersBeforeSavingNewOne = managerRepository.findAll();
        //when
        ManagerDto createdManager = managerService.create(newManager);
        List<ManagerEntity> managersAfterSavingOne = managerRepository.findAll();
        //then
        assertNotEquals(managersBeforeSavingNewOne.size(), managersAfterSavingOne.size());
        assertEquals(managersBeforeSavingNewOne.size() + 1, managersAfterSavingOne.size());
        assertTrue(managerRepository.existsById(createdManager.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingManagerWhoHasId() {
        //given
        ManagerDto newManager = ManagerDto.builder()
                .login("jwick")
                .password("password123")
                .name("John")
                .lastName("Wick")
                .email("jwick@gmail.com")
                .phoneNumber("666666666")
                .id(999L)
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> managerService.create(newManager));
    }

    @Test
    void shouldUpdateManagerWhenUpdatingManager() {
        //given
        ManagerDto managerToUpdate = managerProvider.getAll()
                .get(0);
        String oldName = managerToUpdate.getName();
        String newName = "John";
        managerToUpdate.setName(newName);
        //when
        managerService.update(managerToUpdate);
        ManagerDto updatedManager = managerProvider.getById(managerToUpdate.getId());
        //then
        assertNotEquals(oldName, newName);
        assertEquals(managerToUpdate.getId(), updatedManager.getId());
        assertEquals(newName, updatedManager.getName());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingManagerWithNullID() {
        //given
        ManagerDto managerToUpdate = managerProvider.getAll()
                .get(0);
        managerToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.update(managerToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingManagerWithIDNotPresentInDB() {
        //given
        ManagerDto managerToUpdate = managerProvider.getAll()
                .get(0);
        managerToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> managerService.update(managerToUpdate));
    }

    @Test
    void shouldReturnUpdateManagerWhenUpdateAvatar() throws IOException {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("updateFile.xD", data);
        MultipartFile newFile = new MockMultipartFile("updateFile.xD", data);
        ManagerDto managerToUpdate = managerProvider.getAll()
                .get(0);
        managerToUpdate.setAvatar(file.getBytes());
        managerRepository.save(managerMapper.dto2Entity(managerToUpdate));
        // when
        ManagerDto updatedManager = managerService.updateAvatar(managerToUpdate.getId(), newFile);
        // then
        assertEquals(managerToUpdate.getId(), updatedManager.getId());
        assertNotEquals(managerToUpdate.getAvatar(), updatedManager.getAvatar());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullID() {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xd", data);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.updateAvatar(null, file));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullFile() {
        //given
        Long id = managerRepository.findAll()
                .get(0)
                .getId();
        //when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.updateAvatar(id, null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullIDAndNullFile() {
        //given + when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.updateAvatar(null, null));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingAvatarForManagerThatNotExist() {
        //given
        Long id = Long.MAX_VALUE;
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        // when + then
        assertThrows(NotFoundException.class, () -> managerService.updateAvatar(id, file));
    }

    @Test
    void shouldDeleteManagerWhenDeletingManager() {
        //given
        List<ManagerDto> managersBeforeDeletingOne = managerProvider.getAll();
        ManagerDto managerToDeleteDto = managerProvider.getAll()
                .get(0);
        Long id = managerToDeleteDto.getId();
        //when
        managerService.delete(id);
        List<ManagerEntity> managersAfterDeletingOne = managerRepository.findAll();
        //then
        assertFalse(managerRepository.existsById(id));
        assertNotEquals(managersBeforeDeletingOne.size(), managersAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingManagerWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> managerService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingManagerWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.delete(null));
    }

}