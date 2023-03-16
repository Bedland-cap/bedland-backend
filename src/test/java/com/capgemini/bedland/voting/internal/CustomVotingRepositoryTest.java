package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.manager.internal.ManagerRepository;
import com.capgemini.bedland.voting.api.VotingEntity;
import com.capgemini.bedland.voting.internal.custom.CustomVotingRepository;
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
class CustomVotingRepositoryTest {

    @Autowired
    CustomVotingRepository customVotingRepository;

    @Autowired
    VotingRepository votingRepository;

    @Autowired
    ManagerRepository managerRepository;

    @Test
    void shouldFindAllVotingForGivenManagerWhenFindingAllVotingsForGivenManager() {
        //given
        Long sampleManagerId = managerRepository.findAll().get(0).getId();
        List<VotingEntity> expectedVotingsForGivenManagerId = votingRepository.findAll().stream().filter(m -> m.getBuildingEntity().getManagerEntity().getId().equals(sampleManagerId)).toList();
        //when
        List<VotingEntity> foundVotings = customVotingRepository.findAllVotingsForGivenManager(sampleManagerId);
        //then
        assertNotEquals(votingRepository.findAll(), foundVotings);
        assertEquals(expectedVotingsForGivenManagerId.size(), foundVotings.size());
        assertEquals(expectedVotingsForGivenManagerId, foundVotings);
    }

    @Test
    void shouldReturnEmptyListWhenGivenManagerDoesntHaveAssignedVoting() {
        //given
        Long sampleManagerId = 3L;
        //when
        List<VotingEntity> emptyVotingList = customVotingRepository.findAllVotingsForGivenManager(sampleManagerId);
        //then
        assertTrue(emptyVotingList.isEmpty());
    }

}
