package com.capgemini.bedland.services;

import com.capgemini.bedland.dataPreparation.EntityPreparator;
import com.capgemini.bedland.dtos.OwnerDto;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.OwnerMapper;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.services.impl.OwnerServiceImpl;
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
class OwnerServiceImplTest {

    @Autowired
    private OwnerServiceImpl ownerService;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private OwnerMapper ownerMapper;

    @AfterEach
    void cleanUp() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnAllOwnersWhenGettingAllOwners() {
        //given
        List<OwnerDto> ownersInDB = ownerMapper.entities2DTOs(ownerRepository.findAll());
        //when
        List<OwnerDto> ownersReceivedWhenGettingALl = ownerService.getAll();
        //then
        assertNotEquals(0, ownersInDB.size());
        assertEquals(ownersInDB.size(), ownersReceivedWhenGettingALl.size());
        assertEquals(ownersInDB, ownersReceivedWhenGettingALl);

    }

    @Test
    void shouldFindOwnerByIdWhenGettingOwnerById() {
        //given
        OwnerDto owner = ownerMapper.entity2Dto(ownerRepository.findAll()
                .get(0));
        //when
        OwnerDto foundOwner = ownerService.getById(owner.getId());
        //then
        assertNotEquals(null, owner.getId());
        assertNotEquals(null, foundOwner);
        assertEquals(owner, foundOwner);
    }

    ///
    @Test
    void shouldThrowNotFoundExceptionWhenFindingOwnerByIdAndOwnerIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> ownerService.getById(sampleID));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingOwnerByIdAndOwnerAndGivenIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> ownerService.getById(sampleID));
    }

    @Test
    void shouldFindOwnerAvatarByOwnerIdWhenGettingOwnerById() throws IOException {
        //given
        OwnerEntity entity = ownerRepository.findAll()
                .get(0);
        byte[] data = {1, 2};
        MultipartFile file = new MockMultipartFile("name.xD", data);
        //when
        mockStatic(ImageUtil.class);
        when(ImageUtil.decompressImage(data)).thenReturn(data);
        entity.setAvatar(file.getBytes());
        ownerRepository.save(entity);
        //then
        assertEquals(ownerService.getAvatarByOwnerId(entity.getId()), entity.getAvatar());
    }

    @Test
    void shouldCreateOwnerWhenCreatingManager() {
        //given
        OwnerDto dto = ownerMapper.entity2Dto(EntityPreparator.prepareFirstTestOwner());
        List<OwnerEntity> ownerEntities = ownerRepository.findAll();
        //when
        OwnerDto createdOwner = ownerService.create(dto);
        List<OwnerEntity> ownersAfterSavingOne = ownerRepository.findAll();
        //then
        assertNotEquals(ownerEntities.size(), ownersAfterSavingOne.size());
        assertEquals(ownerEntities.size() + 1, ownersAfterSavingOne.size());
        assertTrue(ownerRepository.existsById(createdOwner.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingOwnerWhoHasId() {
        //given
        OwnerDto dto = ownerMapper.entity2Dto(EntityPreparator.prepareFirstTestOwner());
        dto.setId(999L);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> ownerService.create(dto));
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingNullOwner() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> ownerService.create(null));
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingNullOwner() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> ownerService.update(null));
    }
    @Test
    void shouldUpdateOwnerWhenUpdatingManager() {
        //given
        OwnerDto ownerToUpdate = ownerService.getAll()
                .get(0);
        String oldName = ownerToUpdate.getName();
        String newName = "John";
        ownerToUpdate.setName(newName);
        //when
        ownerService.update(ownerToUpdate);
        OwnerDto updatedManager = ownerService.getById(ownerToUpdate.getId());
        //then
        assertNotEquals(oldName, newName);
        assertEquals(ownerToUpdate.getId(), updatedManager.getId());
        assertEquals(newName, updatedManager.getName());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingOwnerWithNullID() {
        //given
        OwnerDto ownerDto = ownerService.getAll()
                .get(0);
        ownerDto.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.update(ownerDto));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingOwnerWithIDNotPresentInDB() {
        //given
        OwnerDto ownerDto = ownerService.getAll()
                .get(0);
        ownerDto.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> ownerService.update(ownerDto));
    }

    @Test
    void shouldReturnUpdateOwnerWhenUpdateAvatar() throws IOException {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("updateFile.xD", data);
        MultipartFile newFile = new MockMultipartFile("updateFile.xD", data);
        OwnerDto ownerToUpdate = ownerService.getAll()
                .get(0);
        ownerToUpdate.setAvatar(file.getBytes());
        ownerRepository.save(ownerMapper.dto2Entity(ownerToUpdate));
        // when
        OwnerDto updatedOwner = ownerService.updateAvatar(ownerToUpdate.getId(), newFile);
        // then
        assertEquals(ownerToUpdate.getId(), updatedOwner.getId());
        assertNotEquals(ownerToUpdate.getAvatar(), updatedOwner.getAvatar());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullID() {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xd", data);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateAvatar(null, file));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullFile() {
        //given
        Long id = ownerRepository.findAll()
                .get(0)
                .getId();
        //when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateAvatar(id, null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAvatarWithNullIDAndNullFile() {
        //given + when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateAvatar(null, null));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingAvatarForOwnerThatNotExist() {
        //given
        Long id = Long.MAX_VALUE;
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        // when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.updateAvatar(id, file));
    }

    @Test
    void shouldDeleteOwnerWhenDeletingOwner() {
        //given
        List<OwnerDto> ownersBeforeDeletingOne = ownerService.getAll();
        OwnerDto ownerToDelete = ownerService.getAll()
                .get(0);
        Long id = ownerToDelete.getId();
        //when
        ownerService.delete(id);
        List<OwnerEntity> ownersAfterDeletingOne = ownerRepository.findAll();
        //then
        assertFalse(ownerRepository.existsById(id));
        assertNotEquals(ownersBeforeDeletingOne.size(), ownersAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletinOwnerWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> ownerService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingOwnerWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> ownerService.delete(null));
    }
}
