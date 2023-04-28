package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.IncidentDto;

public interface IncidentService {

    IncidentDto create(IncidentDto request);

    void delete(Long id);

    IncidentDto update(IncidentDto request);

}
