package com.capgemini.bedland.voting.internal.custom;

import com.capgemini.bedland.voting.internal.VotingDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting_summary")
public class CustomVotingController {

    private final CustomVotingService customVotingService;

    @GetMapping(params = {"managerId"})
    List<VotingDto> findVotingsForGivenManager(@RequestParam Long managerId) {
        return customVotingService.findAllVotingsForGivenManager(managerId);
    }

}
