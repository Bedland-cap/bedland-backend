package com.capgemini.bedland.repositories;

import com.capgemini.bedland.entities.IncidentStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentStatusRepository extends JpaRepository<IncidentStatusEntity, Long> {

}
