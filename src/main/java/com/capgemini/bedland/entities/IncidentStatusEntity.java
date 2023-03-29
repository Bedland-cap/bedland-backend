package com.capgemini.bedland.entities;

import com.capgemini.bedland.enums.IncidentStatusName;
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

    @Column(nullable = false, length = 50, name = "INCIDENT_STATUS_NAME")
    @Enumerated(EnumType.STRING)
    private IncidentStatusName incidentStatusName;

}
