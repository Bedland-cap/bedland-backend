package com.capgemini.bedland.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class VotingDto extends AbstractDto {

    private Long buildingId;
    private LocalDateTime expirationDate;
    private String title;
    private String description;
}
