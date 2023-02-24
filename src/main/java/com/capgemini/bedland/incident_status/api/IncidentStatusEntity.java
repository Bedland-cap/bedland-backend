package com.capgemini.bedland.incident_status.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.incident.api.IncidentEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "INCIDENT_STATUS")
public class IncidentStatusEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private IncidentEntity incidentEntity;

    @Column(nullable = false, name = "INCIDENT_STATUS_NAME")
    @Enumerated(EnumType.STRING)
    private IncidentStatusName incidentStatusName;

}
