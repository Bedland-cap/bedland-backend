package com.capgemini.bedland.incident_status.internal;

public interface IncidentStatusService {

    IncidentStatusDto create(IncidentStatusDto request);

    /**
     * Function to create new IncidentStatus using Incident ID.
     * Used to automation create new IncidentStatus when user create new Incident.
     * Created IncidenStatus has name CRATED and is connected with new Incident.
     *
     * @param incidentId - ID of Incident
     */
    void createByIncidentId(Long incidentId);

    void delete(Long id);

    IncidentStatusDto update(IncidentStatusDto request);

}
