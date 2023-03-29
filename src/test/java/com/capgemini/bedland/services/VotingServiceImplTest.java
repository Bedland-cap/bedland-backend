package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.VotingMapper;
import com.capgemini.bedland.repositories.VotingRepository;
import com.capgemini.bedland.services.impl.VotingServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
class VotingServiceImplTest {

    @Autowired
    VotingServiceImpl votingService;
    @Autowired
    VotingRepository votingRepository;
    @Autowired
    VotingMapper votingMapper;

    @Test
    void shouldReturnAllVotingWhenGetAllVoting() {
        //given
        VotingDto voting1 = votingMapper.entity2Dto(votingRepository.getById(votingRepository.findAll()
                .get(0)
                .getId()));
        VotingDto voting2 = votingMapper.entity2Dto(votingRepository.getById(votingRepository.findAll()
                .get(1)
                .getId()));
        List<VotingDto> expected = new ArrayList<>(List.of(voting1, voting2));
        //when
        List<VotingDto> resultList = votingService.getAll();
        //then
        assertTrue(resultList.containsAll(expected));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnVotingWhenGetVotingById() {
        //given
        VotingDto expectedVoting = votingMapper.entity2Dto(votingRepository.getById(votingRepository.findAll()
                .get(0)
                .getId()));
        //when
        VotingDto result = votingService.getById(expectedVoting.getId());
        //then
        assertEquals(result, expectedVoting);
    }

    @Test
    void shouldReturnThrowWhenGetVotingByIdAndVotingNotExist() {
        assertThrows(NotFoundException.class, () -> votingService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetVotingByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingService.getById(null));
    }

    @Test
    void shouldReturnCreatedVotingWhenCreateVoting() {
        //given
        VotingDto voting = VotingDto.builder()
                .buildingId(1L)
                .expirationDate(LocalDateTime.now())
                .title("Title")
                .description("Description")
                .build();
        //when
        VotingDto createVoting = votingService.create(voting);
        //then
        assertNotNull(createVoting);
        assertNotNull(createVoting.getId());
        assertTrue(votingRepository.existsById(createVoting.getId()));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingAndGivenVotingHasId() {
        //given
        VotingDto voting = VotingDto.builder()
                .id(100L)
                .buildingId(1L)
                .expirationDate(LocalDateTime.now())
                .title("Title")
                .description("Description")
                .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> votingService.create(voting));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingAndGivenVotingIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingService.create(null));
    }

    @Test
    void shouldDeleteVotingWhenDeleteById() {
        //given
        List<VotingDto> beforeVoting = votingService.getAll();
        Long id = beforeVoting.get(0)
                .getId();
        //when
        votingService.delete(id);
        List<VotingDto> afterVoting = votingService.getAll();
        //then
        assertFalse(votingRepository.existsById(id));
        assertEquals(1, beforeVoting.size() - afterVoting.size());
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndVotingNotExist() {
        assertThrows(NotFoundException.class, () -> votingService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingService.delete(null));
    }

    @Test
    void shouldReturnUpdatedVotingWhenUpdate() {
        //given
        VotingDto voting = votingService.getById(votingService.getAll()
                .get(0)
                .getId());
        String updateDescription = "UpdateDescription";
        VotingDto updateVoting = VotingDto.builder()
                .id(voting.getId())
                .buildingId(voting.getBuildingId())
                .expirationDate(voting.getExpirationDate())
                .title(voting.getTitle())
                .description(updateDescription)
                .build();
        //when
        votingService.update(updateVoting);
        VotingDto resultVoting = votingService.getById(voting.getId());
        //then
        assertEquals(resultVoting.getDescription(), updateDescription);
    }

    @Test
    void shouldReturnThrowWhenUpdateVotingAndGivenVotingHasNoId() {
        VotingDto voting = votingService.getById(votingService.getAll()
                .get(0)
                .getId());
        String updateDescription = "UpdateDescription";
        VotingDto updateVoting = VotingDto.builder()
                .buildingId(voting.getBuildingId())
                .expirationDate(voting.getExpirationDate())
                .title(voting.getTitle())
                .description(updateDescription)
                .build();
        assertThrows(IllegalArgumentException.class, () -> votingService.update(updateVoting));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingNotExist() {
        //given
        VotingDto voting = votingService.getById(votingService.getAll()
                .get(0)
                .getId());
        String updateDescription = "UpdateDescription";
        VotingDto updateVoting = VotingDto.builder()
                .id(Long.MAX_VALUE)
                .buildingId(voting.getBuildingId())
                .expirationDate(voting.getExpirationDate())
                .title(voting.getTitle())
                .description(updateDescription)
                .build();
        //when+then
        assertThrows(NotFoundException.class, () -> votingService.update(updateVoting));
    }

}