package com.capgemini.bedland.voting.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
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
