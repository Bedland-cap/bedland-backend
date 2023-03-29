package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.IncidentDto;
import com.capgemini.bedland.providers.IncidentProvider;
import com.capgemini.bedland.services.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incident")
public class IncidentController {

    private final IncidentProvider incidentProvider;
    private final IncidentService incidentService;

    @GetMapping()
    List<IncidentDto> getAll() {
        return incidentProvider.getAll();
    }

    @GetMapping("/{id}")
    IncidentDto getById(@PathVariable Long id) {
        return incidentProvider.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    IncidentDto create(@RequestBody IncidentDto request) {
        return incidentService.create(request);
    }

    @PatchMapping()
    IncidentDto update(@RequestBody IncidentDto request) {
        return incidentService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        incidentService.delete(id);
    }

}
