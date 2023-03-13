package com.capgemini.bedland.voting_option.internal;

import com.capgemini.bedland.voting_option.api.VotingOptionProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/voting-option")
class VotingOptionController {

    private final VotingOptionProvider votingOptionProvider;
    private final VotingOptionService votingOptionService;

    @GetMapping()
    List<VotingOptionDto> getAll() {
        return votingOptionProvider.getAll();
    }

    @GetMapping("/{id}")
    VotingOptionDto findById(@PathVariable Long id) {
        return votingOptionProvider.getById(id);
    }

    @PostMapping()
    @ResponseStatus(CREATED)
    VotingOptionDto create(@RequestBody VotingOptionDto request) {
        return votingOptionService.create(request);
    }

    @PatchMapping()
    VotingOptionDto update(@RequestBody VotingOptionDto request) {
        return votingOptionService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        votingOptionService.delete(id);
    }

}
