package com.capgemini.bedland.flat.api;

import com.capgemini.bedland.abstract_entity.AbstractEntity;
import com.capgemini.bedland.announcement.api.AnnouncementEntity;
import com.capgemini.bedland.building.api.BuildingEntity;
import com.capgemini.bedland.incident.api.IncidentEntity;
import com.capgemini.bedland.member.api.MemberEntity;
import com.capgemini.bedland.payment.api.PaymentEntity;
import com.capgemini.bedland.voting_response.api.VotingResponseEntity;
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
