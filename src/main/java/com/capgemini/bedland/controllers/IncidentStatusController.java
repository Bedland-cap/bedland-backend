package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.IncidentStatusDto;
import com.capgemini.bedland.providers.IncidentStatusProvider;
import com.capgemini.bedland.services.IncidentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incident-status")
public class IncidentStatusController {

    private final IncidentStatusService incidentStatusService;

    private final IncidentStatusProvider incidentStatusProvider;

    @GetMapping()
    List<IncidentStatusDto> getAll() {
        return incidentStatusProvider.getAll();
    }

    @GetMapping("/{id}")
    IncidentStatusDto getById(@PathVariable Long id) {
        return incidentStatusProvider.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    IncidentStatusDto create(@RequestBody IncidentStatusDto request) {
        return incidentStatusService.create(request);
    }

    @PatchMapping()
    IncidentStatusDto update(@RequestBody IncidentStatusDto request) {
        return incidentStatusService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        incidentStatusService.delete(id);
    }

}
