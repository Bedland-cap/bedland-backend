package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingDetailsDto;
import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.entities.VotingEntity;
import com.capgemini.bedland.entities.VotingOptionEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.VotingMapper;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.VotingRepository;
import com.capgemini.bedland.services.impl.VotingServiceImpl;
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
class CustomVotingServiceImplTest {

    @Autowired
    private VotingServiceImpl votingService;
    @Autowired
    private VotingRepository votingRepository;
    @Autowired
    private VotingMapper votingMapper;
    @Autowired
    private CustomVotingService customVotingService;
    @Autowired
    private ManagerRepository managerRepository;

    @Test
    void shouldFindAllVotingForGivenManager_WhenFindingAllVotingsForGivenManager() {
        //given
        Long sampleManagerId = managerRepository.findAll().get(0).getId();
        List<VotingDto> expectedVotingsForGivenManagerId = votingMapper.entities2DTO(votingRepository.findAll().stream().filter(m -> m.getBuildingEntity().getManagerEntity().getId().equals(sampleManagerId)).toList());
        //when
        List<VotingDto> foundVotings = customVotingService.findAllVotingsForGivenManager(sampleManagerId);
        //then
        assertNotEquals(votingService.getAll(), foundVotings);
        assertEquals(expectedVotingsForGivenManagerId.size(), foundVotings.size());
        assertEquals(expectedVotingsForGivenManagerId, foundVotings);
    }

    @Test
    void shouldReturnEmptyList_WhenGivenManagerDoesntHaveAssignedVoting() {
        //given
        Long sampleManagerId = 3L;
        //when
        List<VotingDto> foundVotings = customVotingService.findAllVotingsForGivenManager(sampleManagerId);
        //then
        assertTrue(foundVotings.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenFindingAllVotingsForGivenManagerAndGivenManagerIdIsNull() {
        //given
        Long sampleManagerId = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customVotingService.findAllVotingsForGivenManager(sampleManagerId));
    }

    @Test
    void shouldThrowNotFoundException_WhenFindingAllVotingsForGivenManagerAndGivenManagerIdIsNotInDB() {
        //given
        Long sampleManagerId = 9999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> customVotingService.findAllVotingsForGivenManager(sampleManagerId));
    }

    @Test
    void findOptionsWithNumberOfResponsesForGivenVoting_WhenfindingOptionsWithNumberOfResponsesForGivenVoting() {
        //given
        VotingEntity votingEntity = votingRepository.findAll().get(0);
        List<VotingOptionEntity> votingOptionEntities = votingEntity.getVotingOptionEntities();
        List<VotingDetailsDto> expectedVotingDetailsDtos = new ArrayList<>();
        votingOptionEntities.forEach(voe -> expectedVotingDetailsDtos.add(VotingDetailsDto.builder().votingOptionTitle(voe.getTitle()).amountOfResponses(voe.getVotingResponseEntities().size()).build()));
        //when
        List<VotingDetailsDto> foundVotingDetailsDtos = customVotingService.findOptionsWithNumberOfResponsesForGivenVoting(votingEntity.getId());
        //then
        assertEquals(expectedVotingDetailsDtos,foundVotingDetailsDtos);
    }
    @Test
    void shouldThrowNotFoundExceptionWhenfindingOptionsWithNumberOfResponsesForGivenVoting_AndVotingIsNotInDB() {
        //given
        Long id = 9999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> customVotingService.findOptionsWithNumberOfResponsesForGivenVoting(id));
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenfindingOptionsWithNumberOfResponsesForGivenVoting_AndVotingIdIsNull() {
        //given
        Long id = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customVotingService.findOptionsWithNumberOfResponsesForGivenVoting(id));
    }
}
