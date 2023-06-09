package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.VotingResponseDto;
import com.capgemini.bedland.providers.VotingResponseProvider;
import com.capgemini.bedland.services.VotingResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting-response")
public class VotingResponseController {

    private final VotingResponseProvider votingResponseProvider;

    private final VotingResponseService votingResponseService;

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
