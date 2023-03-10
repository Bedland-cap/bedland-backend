package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.building.internal.BuildingDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.flat.api.FlatProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
public class FlatServiceImplTest {

    @Autowired
    private FlatService flatService;

    @Autowired
    private FlatProvider flatProvider;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private FlatMapper flatMapper;

    @Test
    void shouldReturnAllFlatsWhenGettingAllFlats() {
        //given
        List<FlatDto> flatsInDB = flatMapper.entities2DTOs(flatRepository.findAll());
        //when
        List<FlatDto>flatsFound = flatProvider.getAll();
        //then
        assertNotEquals(0, flatsInDB.size());
        assertEquals(flatsInDB.size(), flatsFound.size());
        assertEquals(flatsInDB, flatsFound);
    }

    @Test
    void shouldFIndFlatByIdWhenGettingFlatById() {
        //given
        FlatDto flatInDB = flatMapper.entity2Dto(flatRepository.findAll().get(0));
        //when
        FlatDto flatFound =flatProvider.getById(flatInDB.getId());
        //then
        assertNotEquals(null, flatFound.getId());
        assertNotEquals(null, flatFound);
        assertEquals(flatInDB, flatFound);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingFlatByIdAndFlatIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> flatProvider.getById(sampleID));
    }

    @Test
    void shouldIllegalArgumentExceptionWhenFindingFlatByIdAndIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> flatProvider.getById(sampleID));
    }

    @Test
    void shouldCreateFlatWhenCreatingFlat() {
        //given
        FlatDto newFlat = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").build();
        List<FlatEntity> flatsBeforeSavingNewOne = flatRepository.findAll();
        //when
        FlatDto createdFlat = flatService.create(newFlat);
        List<FlatEntity> flatsAfterSavingOne =flatRepository.findAll();
        //then
        assertNotEquals(flatsBeforeSavingNewOne.size(), flatsAfterSavingOne.size());
        assertEquals(flatsBeforeSavingNewOne.size() + 1, flatsAfterSavingOne.size());
        assertTrue(flatRepository.existsById(createdFlat.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingFlatWhichHasID() {
        //given
        FlatDto newFlat = FlatDto.builder().buildingId(1L).number("4").floor(1).shapePath("dasdasd23413").build();
        newFlat.setId(999L);
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> flatService.create(newFlat));
    }

    @Test
    void shouldDeleteFlatWhenDeletingFlat() {
        //given
        List<FlatDto> flatsBeforeDeletingOne = flatProvider.getAll();
        FlatDto flatToDeleteDto =flatProvider.getAll().get(0);
        Long id = flatToDeleteDto.getId();
        //when
        flatService.delete(id);
        List<FlatEntity>flatsAfterDeletingOne = flatRepository.findAll();
        //then
        assertFalse(flatRepository.existsById(id));
        assertNotEquals(flatsBeforeDeletingOne.size(),flatsAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingFlatWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> flatService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingFlatWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> flatService.delete(null));
    }

    @Test
    void shouldUpdateFlatWhenUpdatingFlat() {
        //given
        FlatDto flatToUpdate = flatProvider.getAll().get(0);
        String oldNumber = flatToUpdate.getNumber();
        String newNumber = "22323";
        flatToUpdate.setNumber(newNumber);
        //when
        flatService.update(flatToUpdate);
        FlatDto updatedFlat = flatProvider.getById(flatToUpdate.getId());
        //then
        assertNotEquals(oldNumber, newNumber);
        assertEquals(flatToUpdate.getId(), updatedFlat.getId());
        assertEquals(newNumber, updatedFlat.getNumber());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingFlatWithNullID() {
        //given
        FlatDto flatToUpdate = flatProvider.getAll().get(0);
        flatToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> flatService.update(flatToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingFlatWithIDNotPresentInDB() {
        //given
        FlatDto flatToUpdate =flatProvider.getAll().get(0);
        flatToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> flatService.update(flatToUpdate));
    }

}
