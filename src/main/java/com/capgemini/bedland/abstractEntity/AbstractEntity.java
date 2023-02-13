package com.capgemini.bedland.abstractEntity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Version;

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Version
    private Long version;

    private LocalDateTime createDate;

    private LocalDateTime updateDate;

    @PrePersist
    public void createDate() {
        this.createDate = LocalDateTime.now().truncatedTo(MILLIS);
        this.updateDate = createDate;
    }

    @PreUpdate
    public void updateDate() {
        this.updateDate = LocalDateTime.now().truncatedTo(MILLIS);
    }

}
