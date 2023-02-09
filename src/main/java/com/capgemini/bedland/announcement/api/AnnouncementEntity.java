package com.capgemini.bedland.announcement.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.flat.api.FlatEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "ANNOUNCEMENT")
public class AnnouncementEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private BuildingEntity buildingEntity;

    @Column(nullable = false, length = 125, name = "TITLE")
    private String title;

    @Column(nullable = false, name = "DESCRIPTION")
    private String description;

}
