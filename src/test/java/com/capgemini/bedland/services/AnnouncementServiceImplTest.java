package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.entities.AnnouncementEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.AnnouncementMapper;
import com.capgemini.bedland.providers.AnnouncementProvider;
import com.capgemini.bedland.repositories.AnnouncementRepository;
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
class AnnouncementServiceImplTest {

    @Autowired
    private AnnouncementProvider announcementProvider;

    @Autowired
    private AnnouncementService announcementService;
    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Test
    void shouldReturnAllAnnouncementsWhenGettingAllAnnouncements() {
        //given
        List<AnnouncementDto> announcementsInDB = announcementMapper.entities2DTOs(announcementRepository.findAll());
        //when
        List<AnnouncementDto> announcementsReceivedWhenGettingALl = announcementProvider.getAll();
        //then
        assertNotEquals(0, announcementsInDB.size());
        assertEquals(announcementsInDB.size(), announcementsReceivedWhenGettingALl.size());
        assertEquals(announcementsInDB, announcementsReceivedWhenGettingALl);
    }

    @Test
    void shouldFindAnnouncementByIdWhenGettingAnnouncementById() {
        //given
        AnnouncementDto expectedAnnouncement = announcementMapper.entity2Dto(announcementRepository.findAll().get(0));
        //when
        AnnouncementDto foundAnnouncement = announcementProvider.getById(expectedAnnouncement.getId());
        //then
        assertNotEquals(null, expectedAnnouncement.getId());
        assertNotEquals(null, foundAnnouncement);
        assertEquals(expectedAnnouncement, foundAnnouncement);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingManagerByIdAndAnnouncementIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> announcementProvider.getById(sampleID));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingAnnouncementByIdAndAnnouncementAndGivenIdIsNull() {
        //given
        Long sampleID = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> announcementProvider.getById(sampleID));
    }

    @Test
    void shouldCreateAnnouncementWhenCreatingAnnouncement() {
        //given
        AnnouncementDto newAnnouncement = AnnouncementDto.builder().title("test announcement").title("test title").description("test description").flatId(1L).buildingId(1L).build();

        List<AnnouncementEntity> announcementsBeforeSavingNewOne = announcementRepository.findAll();
        //when
        AnnouncementDto createdAnnouncement = announcementService.create(newAnnouncement);
        List<AnnouncementEntity> announcementsAfterSavingOne = announcementRepository.findAll();
        //then
        assertNotEquals(announcementsBeforeSavingNewOne.size(), announcementsAfterSavingOne.size());
        assertEquals(announcementsBeforeSavingNewOne.size() + 1, announcementsAfterSavingOne.size());
        assertTrue(announcementRepository.existsById(createdAnnouncement.getId()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenCreatingManagerWhoHasId() {
        //given
        AnnouncementDto newAnnouncement = AnnouncementDto.builder().title("test announcement").title("test title").description("test description").flatId(1L).buildingId(1L).id(999L).build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> announcementService.create(newAnnouncement));
    }

    @Test
    void shouldUpdateAnnouncementWhenUpdatingAnnouncement() {
        //given
        AnnouncementDto announcementToUpdate = announcementProvider.getAll().get(0);
        String oldTitle = announcementToUpdate.getTitle();
        String newTitle = "Przetarg na sprzątanie";
        announcementToUpdate.setTitle(newTitle);
        //when
        announcementService.update(announcementToUpdate);
        AnnouncementDto updatedAnnouncement = announcementProvider.getById(announcementToUpdate.getId());
        //then
        assertNotEquals(oldTitle, newTitle);
        assertEquals(announcementToUpdate.getId(), updatedAnnouncement.getId());
        assertEquals(newTitle, updatedAnnouncement.getTitle());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenUpdatingAnnouncementWithNullID() {
        //given
        AnnouncementDto announcementToUpdate = announcementProvider.getAll().get(0);
        announcementToUpdate.setId(null);
        //when + then
        assertThrows(IllegalArgumentException.class, () -> announcementService.update(announcementToUpdate));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingAnnouncementWithIDNotPresentInDB() {
        //given
        AnnouncementDto announcementToUpdate = announcementProvider.getAll().get(0);
        announcementToUpdate.setId(99999L);
        //when + then
        assertThrows(NotFoundException.class, () -> announcementService.update(announcementToUpdate));
    }

    @Test
    void shouldDeleteAnnouncementWhenDeletingAnnouncement() {
        //given
        List<AnnouncementDto> announcementsBeforeDeletingOne = announcementProvider.getAll();
        AnnouncementDto announcementToDeleteDto = announcementProvider.getAll().get(0);
        Long id = announcementToDeleteDto.getId();
        //when
        announcementService.delete(id);
        List<AnnouncementEntity> announcementsAfterDeletingOne = announcementRepository.findAll();
        //then
        assertFalse(announcementRepository.existsById(id));
        assertNotEquals(announcementsBeforeDeletingOne.size(), announcementsAfterDeletingOne.size());
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingAnnouncementWithIDNotPresentInDB() {
        //given
        //when + then
        assertThrows(NotFoundException.class, () -> announcementService.delete(99999L));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenDeletingAnnouncementWithNullID() {
        //given
        //when + then
        assertThrows(IllegalArgumentException.class, () -> announcementService.delete(null));
    }
}



