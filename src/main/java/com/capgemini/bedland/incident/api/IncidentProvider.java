package com.capgemini.bedland.incident.api;

import com.capgemini.bedland.incident.internal.IncidentDto;

import java.util.List;

public interface IncidentProvider {

    List<IncidentDto> getAll();

    IncidentDto getById(Long id);

}