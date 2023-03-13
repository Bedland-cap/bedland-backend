package com.capgemini.bedland.voting_option.internal;

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
class VotingOptionServiceImplTest {

    @Autowired
    VotingOptionServiceImpl votingOptionService;
    @Autowired
    VotingOptionRepository votingOptionRepository;
    @Autowired
    VotingOptionMapper votingOptionMapper;

    @Test
    void shouldReturnAllVotingOptionWhenGetAllVotingOption() {
        //given
        VotingOptionDto votingOption1 = votingOptionMapper.entity2Dto(votingOptionRepository.getById(
                votingOptionRepository.findAll()
                                      .get(0)
                                      .getId()));
        VotingOptionDto votingOption2 = votingOptionMapper.entity2Dto(votingOptionRepository.getById(
                votingOptionRepository.findAll()
                                      .get(1)
                                      .getId()));
        List<VotingOptionDto> expected = new ArrayList<>(List.of(votingOption1, votingOption2));
        //when
        List<VotingOptionDto> resultList = votingOptionService.getAll();
        //then
        assertTrue(resultList.containsAll(expected));
        assertTrue(resultList.size() > 0);
    }

    @Test
    void shouldReturnVotingOptionWhenGetVotingOptionById() {
        //given
        VotingOptionDto expectedVotingOption = votingOptionMapper.entity2Dto(votingOptionRepository.getById(
                votingOptionRepository.findAll()
                                      .get(0)
                                      .getId()));
        //when
        VotingOptionDto result = votingOptionService.getById(expectedVotingOption.getId());
        //then
        assertEquals(result, expectedVotingOption);
    }

    @Test
    void shouldReturnThrowWhenGetVotingOptionByIdAndVotingOptionNotExist() {
        assertThrows(NotFoundException.class, () -> votingOptionService.getById(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenGetVotingOptionByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.getById(null));
    }

    @Test
    void shouldReturnCreatedVotingOptionWhenCreateVotingOption() {
        //given
        VotingOptionDto votingOptionDto = VotingOptionDto.builder()
                                                         .votingId(1L)
                                                         .title("Title")
                                                         .description("Description")
                                                         .build();
        //when
        VotingOptionDto createVotingOption = votingOptionService.create(votingOptionDto);
        //then
        assertNotNull(createVotingOption);
        assertNotNull(createVotingOption.getId());
        assertTrue(votingOptionRepository.existsById(createVotingOption.getId()));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingOptionAndGivenVotingOptionHasId() {
        //given
        VotingOptionDto votingOptionDto = VotingOptionDto.builder()
                                                         .id(100L)
                                                         .votingId(1L)
                                                         .title("Title")
                                                         .description("Description")
                                                         .build();
        //when+then
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.create(votingOptionDto));
    }

    @Test
    void shouldReturnThrowWhenCreateVotingOptionAndGivenVotingOptionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.create(null));
    }

    @Test
    void shouldDeleteVotingOptionWhenDeleteById() {
        //given
        List<VotingOptionDto> beforeVotingOption = votingOptionService.getAll();
        Long id = beforeVotingOption.get(0)
                                    .getId();
        //when
        votingOptionService.delete(id);
        List<VotingOptionDto> afterVotingOption = votingOptionService.getAll();
        //then
        assertFalse(votingOptionRepository.existsById(id));
        assertEquals(1, beforeVotingOption.size() - afterVotingOption.size());
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndVotingOptionNotExist() {
        assertThrows(NotFoundException.class, () -> votingOptionService.delete(Long.MAX_VALUE));
    }

    @Test
    void shouldReturnThrowWhenDeleteByIdAndGivenIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.delete(null));
    }

    @Test
    void shouldReturnUpdatedVotingOptionWhenUpdate() {
        //given
        VotingOptionDto votingOption = votingOptionService.getById(votingOptionService.getAll()
                                                                                      .get(0)
                                                                                      .getId());
        String updateDescription = "UpdateDescription";
        VotingOptionDto updateVotingOption = VotingOptionDto.builder()
                                                            .id(votingOption.getId())
                                                            .votingId(1L)
                                                            .title("Title")
                                                            .description(updateDescription)
                                                            .build();
        //when
        votingOptionService.update(updateVotingOption);
        VotingOptionDto resultVotingOption = votingOptionService.getById(votingOption.getId());
        //then
        assertEquals(resultVotingOption.getDescription(), updateDescription);
    }

    @Test
    void shouldReturnThrowWhenUpdateVotingOptionAndGivenVotingOptionHasNoId() {
        VotingOptionDto votingOption = votingOptionService.getById(votingOptionService.getAll()
                                                                                      .get(0)
                                                                                      .getId());
        String updateDescription = "UpdateDescription";
        VotingOptionDto updateVotingOption = VotingOptionDto.builder()
                                                            .votingId(1L)
                                                            .title("Title")
                                                            .description("Description")
                                                            .build();
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.update(updateVotingOption));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingOptionIsNull() {
        assertThrows(IllegalArgumentException.class, () -> votingOptionService.update(null));
    }

    @Test
    void shouldReturnThrowWhenUpdateAndGivenVotingOptionNotExist() {
        //given
        VotingOptionDto votingOption = votingOptionService.getById(votingOptionService.getAll()
                                                                                      .get(0)
                                                                                      .getId());
        String updateDescription = "UpdateDescription";
        VotingOptionDto updateVotingOption = VotingOptionDto.builder()
                                                            .id(Long.MAX_VALUE)
                                                            .votingId(1L)
                                                            .title("Title")
                                                            .description("Description")
                                                            .build();
        //when+then
        assertThrows(NotFoundException.class, () -> votingOptionService.update(updateVotingOption));
    }

}