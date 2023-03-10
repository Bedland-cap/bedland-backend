package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.manager.api.ManagerEntity;
import com.capgemini.bedland.manager.api.ManagerProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
public class ManagerServiceImplTest {

    @Autowired
    private ManagerProvider managerProvider;

    @Autowired
    private ManagerService managerService;
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ManagerMapper managerMapper;

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
        ManagerDto expectedManager = managerMapper.entity2Dto(managerRepository.findAll().get(0));
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
    void shouldCreateManagerWhenCreatingManager() {
        //given
        ManagerDto newManager = ManagerDto.builder().login("jwick").password("password123").name("John").lastName("Wick").email("jwick@gmail.com").phoneNumber("666666666").build();
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
        ManagerDto newManager = ManagerDto.builder().login("jwick").password("password123").name("John").lastName("Wick").email("jwick@gmail.com").phoneNumber("666666666").id(999L).build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> managerService.create(newManager));
    }

    @Test
    void shouldUpdateManagerWhenUpdatingManager() {
        //given
        ManagerDto managerToUpdate = managerProvider.getAll().get(0);
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
        ManagerDto managerToUpdate = managerProvider.getAll().get(0);
        managerToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> managerService.update(managerToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingManagerWithIDNotPresentInDB() {
        //given
        ManagerDto managerToUpdate = managerProvider.getAll().get(0);
        managerToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> managerService.update(managerToUpdate));
    }


    @Test
    void shouldDeleteManagerWhenDeletingManager() {
        //given
        List<ManagerDto> managersBeforeDeletingOne = managerProvider.getAll();
        ManagerDto managerToDeleteDto = managerProvider.getAll().get(0);
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