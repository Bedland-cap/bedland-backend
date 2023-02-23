package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.voting.api.VotingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/voting")
class VotingController {

    @Autowired
    private VotingService votingService;
    @Autowired
    private VotingProvider votingProvider;

    @GetMapping()
    List<VotingDto> getAll() {
        return votingProvider.getAll();
    }

    @GetMapping("/{id}")
    VotingDto findById(@PathVariable Long id) {
        return votingProvider.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    VotingDto create(@RequestBody VotingDto request) {
        return votingService.create(request);
    }

    @PatchMapping()
    VotingDto update(@RequestBody VotingDto request) {
        return votingService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        votingService.delete(id);
    }

}