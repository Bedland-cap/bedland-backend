package com.capgemini.bedland.incident.internal;

interface IncidentService {

    IncidentDto create(IncidentDto request);

    void delete(Long id);

    IncidentDto update(IncidentDto request);

}
