package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.building.api.BuildingProvider;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.image.ImageUtil;
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
public class BuildingServiceImplTest {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    private BuildingProvider buildingProvider;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingMapper buildingMapper;

    @AfterEach
    void cleanUp() {
        Mockito.clearAllCaches();
    }

    @Test
    void shouldReturnAllBuildingsWhenGettinAllBuildings() {
        //given
        List<BuildingDto> buildingsInDB = buildingMapper.entities2DTOs(buildingRepository.findAll());
        //when
        List<BuildingDto> buildingsFound = buildingProvider.getAll();
        //then
        assertNotEquals(0, buildingsInDB.size());
        assertEquals(buildingsInDB.size(), buildingsFound.size());
        assertEquals(buildingsInDB, buildingsFound);
    }

    @Test
    void shouldFIndBuildingByIdWhenGettingBuildingById() {
        //given
        BuildingDto buildingInDB = buildingMapper.entity2Dto(buildingRepository.findAll()
                                                                               .get(0));
        //when
        BuildingDto buildingFound = buildingProvider.getById(buildingInDB.getId());
        //then
        assertNotEquals(null, buildingFound.getId());
        assertNotEquals(null, buildingFound);
        assertEquals(buildingInDB, buildingFound);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingBuildingByIdAndBuildingIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> buildingProvider.getById(sampleID));
    }

    @Test
    void shouldIllegalArgumentExceptionWhenFindingBuildingByIdAndIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> buildingProvider.getById(sampleID));
    }

    @Test
    void shouldFindBuildingPhotoByBuildingIdWhenGettingBuildingById() throws IOException {
        //given
        BuildingEntity building = buildingRepository.findAll()
                                                    .get(0);
        byte[] data = {1, 2};
        MultipartFile file = new MockMultipartFile("name.xD", data);
        //when
        mockStatic(ImageUtil.class);
        when(ImageUtil.decompressImage(data)).thenReturn(data);
        building.setPhoto(file.getBytes());
        buildingRepository.save(building);
        //then
        assertEquals(buildingProvider.getPhotoByBuildingId(building.getId()), building.getPhoto());
    }

    @Test
    void shouldCreateBuildingWhenCreatingBuilding() {
        //given
        BuildingDto newBuilding = BuildingDto.builder()
                                             .managerId(1L)
                                             .buildingName("test name")
                                             .address("Dubai 2137")
                                             .floors(55)
                                             .build();
        List<BuildingEntity> buildingsBeforeSavingNewOne = buildingRepository.findAll();
        //when
        BuildingDto createdBuilding = buildingService.create(newBuilding);
        List<BuildingEntity> buildingsAfterSavingOne = buildingRepository.findAll();
        //then
        assertNotEquals(buildingsBeforeSavingNewOne.size(), buildingsAfterSavingOne.size());
        assertEquals(buildingsBeforeSavingNewOne.size() + 1, buildingsAfterSavingOne.size());
        assertTrue(buildingRepository.existsById(createdBuilding.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingBuildingWhichHasID() {
        //given
        BuildingDto newBuilding = BuildingDto.builder()
                                             .managerId(1L)
                                             .buildingName("test name")
                                             .address("Dubai 2137")
                                             .floors(55)
                                             .build();
        newBuilding.setId(999L);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> buildingService.create(newBuilding));
    }

    @Test
    void shouldDeleteBuildingWhenDeletingBuilding() {
        //given
        List<BuildingDto> buildingsBeforeDeletingOne = buildingProvider.getAll();
        BuildingDto buildingToDeleteDto = buildingProvider.getAll()
                                                          .get(0);
        Long id = buildingToDeleteDto.getId();
        //when
        buildingService.delete(id);
        List<BuildingEntity> buildingsAfterDeletingOne = buildingRepository.findAll();
        //then
        assertFalse(buildingRepository.existsById(id));
        assertNotEquals(buildingsBeforeDeletingOne.size(), buildingsAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingBuildingWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> buildingService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingBuildingWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> buildingService.delete(null));
    }

    @Test
    void shouldUpdateBuildingWhenUpdatingBuilding() {
        //given
        BuildingDto buildingToUpdate = buildingProvider.getAll()
                                                       .get(0);
        String oldName = buildingToUpdate.getBuildingName();
        String newName = "new name";
        buildingToUpdate.setBuildingName(newName);
        //when
        buildingService.update(buildingToUpdate);
        BuildingDto updatedBuilding = buildingProvider.getById(buildingToUpdate.getId());
        //then
        assertNotEquals(oldName, newName);
        assertEquals(buildingToUpdate.getId(), updatedBuilding.getId());
        assertEquals(newName, updatedBuilding.getBuildingName());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingBuildingWithNullID() {
        //given
        BuildingDto buildingToUpdate = buildingProvider.getAll()
                                                       .get(0);
        buildingToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> buildingService.update(buildingToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingBuildingWithIDNotPresentInDB() {
        //given
        BuildingDto buildingToUpdate = buildingProvider.getAll()
                                                       .get(0);
        buildingToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> buildingService.update(buildingToUpdate));
    }

    @Test
    void shouldReturnUpdatedBuildingWhenUpdatePhoto() throws IOException {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        MultipartFile newFile = new MockMultipartFile("updateFile.xD", data);
        BuildingEntity buildingToUpdate = buildingRepository.findAll()
                                                            .get(0);
        buildingToUpdate.setPhoto(file.getBytes());
        buildingRepository.save(buildingToUpdate);
        // when
        BuildingDto updatedBuilding = buildingService.updatePhoto(buildingToUpdate.getId(), newFile);
        // then
        assertEquals(buildingToUpdate.getId(), updatedBuilding.getId());
        assertNotEquals(file.getBytes(), updatedBuilding.getPhoto());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingPhotoWithNullID() {
        //given
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xd", data);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> buildingService.updatePhoto(null, file));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingPhotoWithNullFile() {
        //given
        Long id = buildingRepository.findAll()
                                    .get(0)
                                    .getId();
        //when + then
        assertThrows(IllegalArgumentException.class, () -> buildingService.updatePhoto(id, null));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingPhotoWithNullIDAndNullFile() {
        //given + when + then
        assertThrows(IllegalArgumentException.class, () -> buildingService.updatePhoto(null, null));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingPhotoForBuildingThatNotExist() {
        //given
        Long id = Long.MAX_VALUE;
        byte[] data = new byte[2];
        MultipartFile file = new MockMultipartFile("file.xD", data);
        // when + then
        assertThrows(NotFoundException.class, () -> buildingService.updatePhoto(id, file));
    }

}
