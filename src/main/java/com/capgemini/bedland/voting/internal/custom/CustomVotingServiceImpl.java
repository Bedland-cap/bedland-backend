package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.manager.internal.ManagerRepository;
import com.capgemini.bedland.voting.internal.VotingDto;
import com.capgemini.bedland.voting.internal.VotingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CustomVotingServiceImpl implements CustomVotingService {

    @Autowired
    private VotingMapper votingMapper;

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CustomVotingRepository customVotingRepository;

    @Override
    public List<VotingDto> findAllVotingsForGivenManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID can't be null");
        }
        if (!managerRepository.existsById(managerId)) {
            throw new NotFoundException("Manager ID doesn't exist in DB");
        }
        return votingMapper.entities2DTO(customVotingRepository.findAllVotingsForGivenManager(managerId));
    }
}
