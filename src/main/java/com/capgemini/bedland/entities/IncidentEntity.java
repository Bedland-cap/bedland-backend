package com.capgemini.bedland.entities;

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

    @Column(nullable = false, name = "TITLE")
    private String title;

    @Column(nullable = false, length = 500, name = "DESCRIPTION")
    private String description;

    @Column(nullable = false, name = "COMMON_SPACE")
    private boolean commonSpace;

}
