package com.capgemini.bedland.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static org.hibernate.Length.LONG32;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "MEMBER")
public class MemberEntity extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
    @JoinColumn
    private FlatEntity flatEntity;

    @Column(nullable = false, length = 50, name = "NAME")
    private String name;

    @Column(nullable = false, length = 50, name = "LAST_NAME")
    private String lastName;

    @Column(nullable = false, length = 50, name = "EMAIL")
    private String email;

    @Column(nullable = false, length = 25, name = "PHONE_NUMBER")
    private String phoneNumber;

    @Column(name = "AVATAR", length = LONG32)
    private byte[] avatar;

}
