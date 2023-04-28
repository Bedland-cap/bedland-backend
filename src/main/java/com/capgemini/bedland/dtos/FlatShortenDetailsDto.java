package com.capgemini.bedland.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class FlatShortenDetailsDto {

    private Long flatId;
    private String flatNumber;
    private String flatAddress;
    private LocalDateTime flatCreateDate;

}
