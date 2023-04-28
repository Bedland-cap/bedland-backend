package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.VotingDetailsDto;
import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.entities.VotingOptionEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.VotingMapper;
import com.capgemini.bedland.repositories.CustomVotingRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.VotingRepository;
import com.capgemini.bedland.services.CustomVotingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class CustomVotingServiceImpl implements CustomVotingService {

    @Autowired
    private VotingMapper votingMapper;
    @Autowired
    private VotingRepository votingRepository;
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

    @Override
    public List<VotingDetailsDto> findOptionsWithNumberOfResponsesForGivenVoting(Long votingId) {
        if (votingId == null) {
            throw new IllegalArgumentException("voting ID can't be null");
        }
        if (!votingRepository.existsById(votingId)) {
            throw new NotFoundException("Voting ID doesn't exist in DB");
        }
        List<VotingDetailsDto> votingDetailsResults = new LinkedList<>();
        List<VotingOptionEntity> optionsForGivenVoting = customVotingRepository.findOptionsForGivenVoting(votingId);

        for (VotingOptionEntity option : optionsForGivenVoting) {
            VotingDetailsDto optionDetail = new VotingDetailsDto();
            optionDetail.setVotingOptionTitle(option.getTitle());
            int responseAmount = customVotingRepository.findResponsesForGivenVotingOption(option.getId()).size();
            optionDetail.setAmountOfResponses(responseAmount);
            votingDetailsResults.add(optionDetail);
        }
        return votingDetailsResults;
    }

}
