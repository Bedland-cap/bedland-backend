package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.IncidentStatusRepository;
import com.capgemini.bedland.services.impl.IncidentStatusServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class IncidentStatusServiceImplTest {

    @Autowired
    private IncidentStatusServiceImpl incidentStatusService;
    @Autowired
    private IncidentStatusRepository incidentStatusRepository;

    @Test
    void shouldReturnAllIncidentStatusWhenGetAllIncidentStatus() {
        //given
        IncidentStatusDto incidentStatus1 = incidentStatusService.getById(1L);
        IncidentStatusDto incidentStatus2 = incidentStatusService.getById(2L);
        List<IncidentStatusDto> incidentStatusList = List.of(incidentStatus1, incidentStatus2);
        //when
        List<IncidentStatusDto> resultList = incidentStatusService.getAll();
        //then
        assertTrue(resultList.containsAll(incidentStatusList));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnIncidentStatusWhenGetIncidentStatusById() {
        //given
        Long incidentStatusId = 2L;
        IncidentStatusDto incidentStatus = IncidentStatusDto.builder()
                .id(incidentStatusId)
                .incidentId(2L)
                .incidentStatusName("CREATED")
                .build();
        //when
        IncidentStatusDto result = incidentStatusService.getById(incidentStatusId);
        //then
        assertEquals(result, incidentStatus);
    }

    @Test
    void shouldReturnThrowWhenGetIncidentStatusByIdAndIncidentStatusNotExist() {
        assertThrows(NotFoundException.class, () -> incidentStatusService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetIncidentStatusByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.getById(null));
    }

    @Test
    void shouldReturnCreatedIncidentStatusWithIncidentStatusNameCreatedWhenCreateIncidentStatus() {
        //given
        IncidentStatusDto incidentStatus = IncidentStatusDto.builder()
                .incidentId(1L)
                .build();
        //when
        IncidentStatusDto createIncidentStatus = incidentStatusService.create(incidentStatus);
        //then
        assertNotNull(createIncidentStatus);
        assertNotNull(createIncidentStatus.getId());
        assertTrue(incidentStatusRepository.existsById(createIncidentStatus.getId()));
        assertEquals("CREATED", createIncidentStatus.getIncidentStatusName());
    }

    @Test
    void shouldReturnThrowWhenCreateIncidentStatusAndGivenIncidentStatusHasId() {
        //given
        IncidentStatusDto incidentStatus = IncidentStatusDto.builder()
                .id(100L)
                .incidentId(1L)
                .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.create(incidentStatus));
    }

    @Test
    void shouldReturnThrowWhenCreateIncidentStatusAndGivenIncidentStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.create(null));
    }

    @Test
    void shouldCreateIncidentStatusWithNameCreatedWhenCreateByIncidentId() {
        //given
        List<IncidentStatusDto> beforeIncidentStatus = incidentStatusService.getAll();
        String expectedName = "CREATED";
        Long incidentId = 1L;
        //when
        incidentStatusService.createByIncidentId(incidentId);
        List<IncidentStatusDto> afterIncidentStatus = incidentStatusService.getAll();
        IncidentStatusDto newIncidentStatus = incidentStatusService.getById(incidentStatusService.getAll()
                .get(afterIncidentStatus.size() - 1)
                .getId());
        //then
        assertEquals(1, afterIncidentStatus.size() - beforeIncidentStatus.size());
        assertSame(expectedName, newIncidentStatus.getIncidentStatusName());
        assertSame(incidentId, newIncidentStatus.getIncidentId());
    }

    @Test
    void shouldReturnThrowWhenCreateByIncidentIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.createByIncidentId(null));
    }

    @Test
    void shouldReturnThrowWhenCreateByIncidentIdAndIncidentWithGivenIdNotExist() {
        assertThrows(NotFoundException.class, () -> incidentStatusService.createByIncidentId(Long.MAX_VALUE));
    }

    @Test
    void shouldDeleteIncidentStatusWhenDeleteById() {
        //given
        List<IncidentStatusDto> beforeIncidentStatus = incidentStatusService.getAll();
        Long id = beforeIncidentStatus.get(0)
                .getId();
        //when
        incidentStatusService.delete(id);
        List<IncidentStatusDto> afterIncidentStatus = incidentStatusService.getAll();
        //then
        assertEquals(1, beforeIncidentStatus.size() - afterIncidentStatus.size());
        assertFalse(incidentStatusRepository.existsById(id));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.delete(null));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIncidentStatusNotExist() {
        assertThrows(NotFoundException.class, () -> incidentStatusService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnUpdatedIncidentStatusWhenUpdate() {
        //given
        Long incidentId = incidentStatusService.getAll()
                .get(0)
                .getId();
        IncidentStatusDto incidentStatus = incidentStatusService.getById(incidentId);
        String updateIncidentStatusName = "IN_PROGRESS";
        IncidentStatusDto updateIncidentStatus = IncidentStatusDto.builder()
                .id(incidentStatus.getId())
                .version(incidentStatus.getVersion())
                .createDate(incidentStatus.getCreateDate())
                .updateDate(incidentStatus.getUpdateDate())
                .incidentId(incidentStatus.getIncidentId())
                .incidentStatusName(updateIncidentStatusName)
                .build();
        //when
        incidentStatusService.update(updateIncidentStatus);
        IncidentStatusDto resultIncidentStatus = incidentStatusService.getById(incidentId);
        //then
        assertEquals(updateIncidentStatus.getId(), resultIncidentStatus.getId());
        assertEquals(resultIncidentStatus.getIncidentStatusName(), updateIncidentStatusName);
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenIncidentStatusIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentStatusService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenIncidentStatusHasNoId() {
        assertThrows(IllegalArgumentException.class,
                () -> incidentStatusService.update(new IncidentStatusDto(1L, "CREATED")));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenIncidentStatusNotExist() {
        //given
        IncidentStatusDto incidentStatus = IncidentStatusDto.builder()
                .id(Long.MAX_VALUE)
                .incidentId(1L)
                .incidentStatusName("IN_PROGRESS")
                .build();
        //when+then
        assertThrows(NotFoundException.class, () -> incidentStatusService.update(incidentStatus));
    }

}