package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingDto;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class CustomVotingServiceImplTest {

    @Autowired
    VotingServiceImpl votingService;
    @Autowired
    VotingRepository votingRepository;
    @Autowired
    VotingMapper votingMapper;

    @Autowired
    CustomVotingService customVotingService;

    @Autowired
    ManagerRepository managerRepository;

    @Test
    void shouldFindAllVotingForGivenManagerWhenFindingAllVotingsForGivenManager() {
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
    void shouldReturnEmptyListWhenGivenManagerDoesntHaveAssignedVoting() {
        //given
        Long sampleManagerId = 3L;
        //when
        List<VotingDto> foundVotings = customVotingService.findAllVotingsForGivenManager(sampleManagerId);
        //then
        assertTrue(foundVotings.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingAllVotingsForGivenManagerAndGivenManagerIdIsNull() {
        //given
        Long sampleManagerId = null;
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customVotingService.findAllVotingsForGivenManager(sampleManagerId));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenFindingAllVotingsForGivenManagerAndGivenManagerIdIsNotInDB() {
        //given
        Long sampleManagerId = 9999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> customVotingService.findAllVotingsForGivenManager(sampleManagerId));
    }
}
