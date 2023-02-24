package com.capgemini.bedland.voting_response.internal;

import com.capgemini.bedland.voting_response.api.VotingResponseProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/voting-response")
class VotingResponseController {

    @Autowired
    private VotingResponseProvider votingResponseProvider;
    @Autowired
    private VotingResponseService votingResponseService;

    @GetMapping()
    List<VotingResponseDto> getAll() {
        return votingResponseProvider.getAll();
    }

    @GetMapping("/{id}")
    VotingResponseDto findById(@PathVariable Long id) {
        return votingResponseProvider.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    VotingResponseDto create(@RequestBody VotingResponseDto request) {
        return votingResponseService.create(request);
    }

    @PatchMapping()
    VotingResponseDto update(@RequestBody VotingResponseDto request) {
        return votingResponseService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        votingResponseService.delete(id);
    }

}