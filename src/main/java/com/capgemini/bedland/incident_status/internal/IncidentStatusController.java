package com.capgemini.bedland.incident_status.internal;

import com.capgemini.bedland.incident_status.api.IncidentStatusProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/incident-status")
class IncidentStatusController {

    @Autowired
    private IncidentStatusService incidentStatusService;
    @Autowired
    private IncidentStatusProvider incidentStatusProvider;

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
