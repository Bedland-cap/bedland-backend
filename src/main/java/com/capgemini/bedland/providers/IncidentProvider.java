package com.capgemini.bedland.providers;

import com.capgemini.bedland.dtos.IncidentDto;

import java.util.List;

public interface IncidentProvider {

    List<IncidentDto> getAll();

    IncidentDto getById(Long id);

}