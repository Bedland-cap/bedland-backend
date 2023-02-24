package com.capgemini.bedland.incident_status.internal;

import com.capgemini.bedland.incident_status.api.IncidentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface IncidentStatusRepository extends JpaRepository<IncidentStatusEntity, Long> {

}
