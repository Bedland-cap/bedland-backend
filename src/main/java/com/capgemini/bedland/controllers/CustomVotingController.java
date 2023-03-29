package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.VotingDetailsDto;
import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.services.CustomVotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CustomVotingController {

    private final CustomVotingService customVotingService;

    @GetMapping(path ="/voting_summary" ,params = {"manager_id"})
    List<VotingDto> findVotingsForGivenManager(@RequestParam Long manager_id) {
        return customVotingService.findAllVotingsForGivenManager(manager_id);
    }

    //todo: tests for below
    @GetMapping(path = "/voting_details",params = {"voting_id"})
    List<VotingDetailsDto> findOptionsWithNumberOfResponsesForGivenVoting(@RequestParam Long voting_id) {
        return customVotingService.findOptionsWithNumberOfResponsesForGivenVoting(voting_id);
    }

}
