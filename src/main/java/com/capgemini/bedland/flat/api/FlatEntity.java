package com.capgemini.bedland.flat.api;

import com.capgemini.bedland.abstractEntity.AbstractEntity;
import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.incident.api.IncidentEntity;
import com.capgemini.bedland.member.api.MemberEntity;
import com.capgemini.bedland.payment.api.PaymentEntity;
import com.capgemini.bedland.votingResponse.api.VotingResponseEntity;
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
    private MemberEntity flatOwner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private BuildingEntity buildingEntity;

    @OneToMany(mappedBy = "flatEntity")
    private List<MemberEntity> flatMembers;

    @OneToMany(mappedBy = "flatEntity",cascade = CascadeType.ALL)
    private List<AnnouncementEntity> announcementEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<IncidentEntity> incidentEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<PaymentEntity> paymentEntities;

    @OneToMany(mappedBy = "flatEntity", cascade = CascadeType.ALL)
    private List<VotingResponseEntity> votingResponseEntities;


    @Column(nullable = false, length = 2, name = "NUMBER")
    private String number;

    @Column(nullable = false, length = 2, name = "FLOOR")
    private int floor;

    @Column(nullable = false, name = "SHAPE_PATH")
    private String shapePath;
}
