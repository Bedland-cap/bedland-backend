package com.capgemini.bedland.incident_status.api;

import com.capgemini.bedland.incident_status.internal.IncidentStatusDto;

import java.util.List;

public interface IncidentStatusProvider {

    List<IncidentStatusDto> getAll();

    IncidentStatusDto getById(Long id);

}
