package com.capgemini.bedland.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerSummaryDto {
    private String buildingName;
    private String flatNumber;
    private int flatFloor;
    private Long flatId;
    private String ownerNameAndLastName;
    private String ownerPhone;
    private Long ownerId;
}
