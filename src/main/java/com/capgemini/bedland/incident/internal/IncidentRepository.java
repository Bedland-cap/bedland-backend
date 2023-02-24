package com.capgemini.bedland.incident.internal;

import com.capgemini.bedland.incident.api.IncidentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentRepository extends JpaRepository<IncidentEntity, Long> {


}
