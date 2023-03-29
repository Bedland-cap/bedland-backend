package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.IncidentStatusDto;

import java.util.List;

public interface IncidentStatusProvider {

    List<IncidentStatusDto> getAll();

    IncidentStatusDto getById(Long id);

}
