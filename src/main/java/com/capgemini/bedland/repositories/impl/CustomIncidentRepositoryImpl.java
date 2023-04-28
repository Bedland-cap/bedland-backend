package com.capgemini.bedland.repositories.impl;

import com.capgemini.bedland.repositories.CustomIncidentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomIncidentRepositoryImpl implements CustomIncidentRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List findLatestCreatedIncidentsForGivenManager(Long managerId, int numberOfIncidents) {
        return entityManager.createQuery("select i from IncidentEntity i " +
                        "join i.flatEntity f " +
                        "join f.buildingEntity b " +
                        "join b.managerEntity m where m.id =" + managerId + " " +
                        "group by i order by i.createDate desc ")
                .setMaxResults(numberOfIncidents).getResultList();

    }

    @Override
    public List findLatestUpdatedIncidentsForGivenOwner(Long ownerId, int numberOfIncidents) {
        return entityManager.createQuery("select i from IncidentEntity i " +
                        "join i.flatEntity f " +
                        "join i.incidentStatusEntities is " +
                        "join f.flatOwnerEntity fo " +
                        "where fo.id =" + ownerId + " and is.incidentStatusName= 'IN_PROGRESS' or is.incidentStatusName= 'SOLVED' " +
                        "group by i order by i.updateDate desc ")
                .setMaxResults(numberOfIncidents).getResultList();
    }
}
