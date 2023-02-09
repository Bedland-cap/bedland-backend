package com.capgemini.bedland.incident.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import com.capgemini.bedland.incidentStatus.api.IncidentStatusEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "INCIDENT")
public class IncidentEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @OneToMany(mappedBy = "incidentEntity", cascade = CascadeType.ALL)
    private List<IncidentStatusEntity> incidentStatusEntities;

    @Column(nullable = false, length = 45, name = "TITLE")
    private String title;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

    @Column(nullable = false, name = "COMMON_SPACE") //todo: maybe use enums like "involves : COMMON_SPACE, FLAT ...etc"
    private boolean commonSpace;

}
