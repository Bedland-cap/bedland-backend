package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.VotingDto;
import com.capgemini.bedland.providers.VotingProvider;
import com.capgemini.bedland.services.VotingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting")
public class VotingController {

    private final VotingService votingService;

    private final VotingProvider votingProvider;

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