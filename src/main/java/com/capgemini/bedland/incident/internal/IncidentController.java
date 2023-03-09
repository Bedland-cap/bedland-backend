package com.capgemini.bedland.incident.internal;

import com.capgemini.bedland.incident.api.IncidentProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incident")
class IncidentController {

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
