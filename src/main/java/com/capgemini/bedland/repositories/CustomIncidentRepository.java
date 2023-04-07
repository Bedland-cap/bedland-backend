package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.IncidentEntity;

import java.util.List;

public interface CustomIncidentRepository {

    List<IncidentEntity> findLatestCreatedIncidentsForGivenManager(Long managerId, int numberOfIncidents);
}
