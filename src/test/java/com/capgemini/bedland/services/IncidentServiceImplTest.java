package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.IncidentDto;
import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.providers.IncidentStatusProvider;
import com.capgemini.bedland.repositories.IncidentRepository;
import com.capgemini.bedland.services.impl.IncidentServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class IncidentServiceImplTest {

    @Autowired
    IncidentServiceImpl incidentService;
    @Autowired
    IncidentRepository incidentRepository;
    @Autowired
    IncidentStatusProvider incidentStatusProvider;

    @Test
    void shouldReturnAllIncidentWhenGetAllIncident() {
        //given
        IncidentDto incident1 = incidentService.getById(1L);
        IncidentDto incident2 = incidentService.getById(2L);
        List<IncidentDto> incidentList = new ArrayList<>(List.of(incident1, incident2));
        //when
        List<IncidentDto> resultList = incidentService.getAll();
        //then
        assertTrue(resultList.containsAll(incidentList));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnIncidentWhenGetIncidentById() {
        //given
        Long id = 1L;
        IncidentDto incident = IncidentDto.builder()
                .id(id)
                .flatId(1L)
                .title("Donos na sąsiada")
                .description(
                        "Uprzejmie donoszę, że sąsiedzi spod 2 nie sprzątają po swoim psie")
                .commonSpace(true)
                .build();
        //when
        IncidentDto result = incidentService.getById(id);
        //then
        assertEquals(result, incident);
    }

    @Test
    void shouldReturnThrowWhenGetIncidentByIdAndIncidentNotExist() {
        assertThrows(NotFoundException.class, () -> incidentService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetIncidentByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentService.getById(null));
    }

    @Test
    void shouldReturnCreatedIncidentWhenCreateIncident() {
        //given
        IncidentDto incident = IncidentDto.builder()
                .flatId(1L)
                .title("Title")
                .description("Description")
                .commonSpace(true)
                .build();
        //when
        IncidentDto createIncident = incidentService.create(incident);
        //then
        assertNotNull(createIncident);
        assertNotNull(createIncident.getId());
        assertTrue(incidentRepository.existsById(createIncident.getId()));
    }

    @Test
    void shouldReturnThrowWhenCreateIncidentAndGivenIncidentHasId() {
        //given
        IncidentDto incident = IncidentDto.builder()
                .id(100L)
                .flatId(1L)
                .title("Title")
                .description("Description")
                .commonSpace(true)
                .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> incidentService.create(incident));
    }

    @Test
    void shouldReturnThrowWhenCreateIncidentAndGivenIncidentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentService.create(null));
    }

    @Test
    void shouldCreateNewIncidentStatusWhenCreateNewIncident() {
        //given
        IncidentDto incident = IncidentDto.builder()
                .flatId(1L)
                .title("Title")
                .description("Description")
                .commonSpace(true)
                .build();
        List<IncidentStatusDto> beforeIncidentStatus = incidentStatusProvider.getAll();
        //when
        IncidentDto createIncident = incidentService.create(incident);
        List<IncidentStatusDto> afterIncidentStatus = incidentStatusProvider.getAll();
        IncidentStatusDto newIncidentStatus = incidentStatusProvider.getById(incidentStatusProvider.getAll()
                .get(
                        afterIncidentStatus.size() - 1)
                .getId());
        //then
        assertEquals(1, afterIncidentStatus.size() - beforeIncidentStatus.size());
        assertSame(createIncident.getId(), newIncidentStatus.getIncidentId());
    }

    @Test
    void shouldDeleteIncidentWhenDeleteById() {
        //given
        List<IncidentDto> beforeIncident = incidentService.getAll();
        Long id = beforeIncident.get(0)
                .getId();
        //when
        incidentService.delete(id);
        List<IncidentDto> afterIncident = incidentService.getAll();
        //then
        assertFalse(incidentRepository.existsById(id));
        assertEquals(1, beforeIncident.size() - afterIncident.size());
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndIncidentNotExist() {
        assertThrows(NotFoundException.class, () -> incidentService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentService.delete(null));
    }

    @Test
    void shouldReturnUpdatedIncidentWhenUpdate() {
        //given
        Long id = 1L;
        IncidentDto incident = incidentService.getById(id);
        String updateTitle = "Update title";
        IncidentDto updateIncident = IncidentDto.builder()
                .id(incident.getId())
                .version(incident.getVersion())
                .createDate(incident.getCreateDate())
                .updateDate(incident.getUpdateDate())
                .flatId(incident.getFlatId())
                .title(updateTitle)
                .description(incident.getDescription())
                .commonSpace(incident.isCommonSpace())
                .build();
        //when
        incidentService.update(updateIncident);
        IncidentDto resultIncident = incidentService.getById(id);
        //then
        assertEquals(resultIncident.getTitle(), updateTitle);
    }

    @Test
    void shouldReturnThrowWhenUpdateIncidentAndGivenIncidentHasNoId() {
        IncidentDto incident = IncidentDto.builder()
                .flatId(1L)
                .title("Title")
                .description("Description")
                .commonSpace(true)
                .build();
        assertThrows(IllegalArgumentException.class, () -> incidentService.update(incident));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenIncidentIsNull() {
        assertThrows(IllegalArgumentException.class, () -> incidentService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenPaymentNotExist() {
        //given
        IncidentDto incident = incidentService.getById(1L);
        String updateTitle = "Update title";
        IncidentDto updateIncident = IncidentDto.builder()
                .id(Long.MAX_VALUE)
                .version(incident.getVersion())
                .createDate(incident.getCreateDate())
                .updateDate(incident.getUpdateDate())
                .flatId(incident.getFlatId())
                .title(updateTitle)
                .description(incident.getDescription())
                .commonSpace(incident.isCommonSpace())
                .build();
        //when+then
        assertThrows(NotFoundException.class, () -> incidentService.update(updateIncident));
    }

}