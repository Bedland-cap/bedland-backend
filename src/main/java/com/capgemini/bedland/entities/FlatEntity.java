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
@Table(name = "FLAT")
public class FlatEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private BuildingEntity buildingEntity;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<MemberEntity> flatMembers;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<AnnouncementEntity> announcementEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<IncidentEntity> incidentEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<VotingResponseEntity> votingResponseEntities;

    @Column(nullable = false, length = 8, name = "NUMBER")
    private String number;

    @Column(nullable = false, name = "FLOOR")
    private int floor;

    @Column(nullable = false, length = 250, name = "SHAPE_PATH")
    private String shapePath;

}
