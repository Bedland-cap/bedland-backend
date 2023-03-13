package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
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
class VotingResponseServiceImplTest {

    @Autowired
    VotingResponseServiceImpl votingResponseService;
    @Autowired
    VotingResponseRepository votingResponseRepository;
    @Autowired
    VotingResponseMapper votingResponseMapper;

    @Test
    void shouldReturnAllVotingResponseWhenGetAllVoting() {
        //given
        VotingResponseDto votingResponse1 = votingResponseMapper.entity2Dto(votingResponseRepository.getById(
                votingResponseRepository.findAll()
                                        .get(0)
                                        .getId()));
        VotingResponseDto votingResponse2 = votingResponseMapper.entity2Dto(votingResponseRepository.getById(
                votingResponseRepository.findAll()
                                        .get(1)
                                        .getId()));
        ;
        List<VotingResponseDto> expected = new ArrayList<>(List.of(votingResponse1, votingResponse2));
        //when
        List<VotingResponseDto> resultList = votingResponseService.getAll();
        //then
        assertTrue(resultList.containsAll(expected));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnVotingResponseWhenGetVotingResponseById() {
        //given
        VotingResponseDto expectedVotingResponse = votingResponseMapper.entity2Dto(votingResponseRepository.getById(
                votingResponseRepository.findAll()
                                        .get(0)
                                        .getId()));
        //when
        VotingResponseDto result = votingResponseService.getById(expectedVotingResponse.getId());
        //then
        assertEquals(result, expectedVotingResponse);
    }

    @Test
    void shouldReturnThrowWhenGetVotingResponseByIdAndVotingResponseNotExist() {
        assertThrows(NotFoundException.class, () -> votingResponseService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetVotingResponseByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.getById(null));
    }

    @Test
    void shouldReturnCreatedVotingResponseWhenCreateVotingResponse() {
        //given
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .votingOptionId(1L)
                                                            .flatId(1L)
                                                            .build();
        //when
        VotingResponseDto createVotingResponse = votingResponseService.create(votingResponse);
        //then
        assertNotNull(createVotingResponse);
        assertNotNull(createVotingResponse.getId());
        assertTrue(votingResponseRepository.existsById(createVotingResponse.getId()));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingResponseAndGivenVotingResponseHasId() {
        //given
        VotingResponseDto votingResponse = VotingResponseDto.builder()
                                                            .id(100L)
                                                            .votingOptionId(1L)
                                                            .flatId(1L)
                                                            .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.create(votingResponse));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingResponseAndGivenVotingResponseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.create(null));
    }

    @Test
    void shouldDeleteVotingResponseWhenDeleteById() {
        //given
        List<VotingResponseDto> beforeVotingResponse = votingResponseService.getAll();
        Long id = beforeVotingResponse.get(0)
                                      .getId();
        //when
        votingResponseService.delete(id);
        List<VotingResponseDto> afterVotingResponse = votingResponseService.getAll();
        //then
        assertFalse(votingResponseRepository.existsById(id));
        assertEquals(1, beforeVotingResponse.size() - afterVotingResponse.size());
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndVotingResponseNotExist() {
        assertThrows(NotFoundException.class, () -> votingResponseService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.delete(null));
    }

    @Test
    void shouldReturnUpdatedVotingResponseWhenUpdate() {
        //given
        VotingResponseDto votingResponse = votingResponseService.getById(votingResponseService.getAll()
                                                                                              .get(0)
                                                                                              .getId());
        Long updateVotingOption = 2L;
        VotingResponseDto updateVotingResponse = VotingResponseDto.builder()
                                                                  .id(votingResponse.getId())
                                                                  .votingOptionId(updateVotingOption)
                                                                  .flatId(votingResponse.getFlatId())
                                                                  .build();
        //when
        votingResponseService.update(updateVotingResponse);
        VotingResponseDto resultVotingResponse = votingResponseService.getById(votingResponse.getId());
        //then
        assertEquals(resultVotingResponse.getVotingOptionId(), updateVotingOption);
    }

    @Test
    void shouldReturnThrowWhenUpdateVotingAndGivenVotingHasNoId() {
        VotingResponseDto votingResponse = votingResponseService.getById(votingResponseService.getAll()
                                                                                              .get(0)
                                                                                              .getId());
        Long updateVotingOption = 2L;
        VotingResponseDto updateVotingResponse = VotingResponseDto.builder()
                                                                  .votingOptionId(updateVotingOption)
                                                                  .flatId(votingResponse.getFlatId())
                                                                  .build();
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.update(updateVotingResponse));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingResponseIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingResponseService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingResponseNotExist() {
        //given
        VotingResponseDto votingResponse = votingResponseService.getById(votingResponseService.getAll()
                                                                                              .get(0)
                                                                                              .getId());
        Long updateVotingOption = 2L;
        VotingResponseDto updateVotingResponse = VotingResponseDto.builder()
                                                                  .id(Long.MAX_VALUE)
                                                                  .votingOptionId(updateVotingOption)
                                                                  .flatId(votingResponse.getFlatId())
                                                                  .build();
        //when+then
        assertThrows(NotFoundException.class, () -> votingResponseService.update(updateVotingResponse));
    }

}