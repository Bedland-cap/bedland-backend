package com.capgemini.bedland.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class OwnerSummaryDto {
    private String buildingName;
    private String flatNumber;
    private int flatFloor;
    private Long flat;
    private String owner;
    private String ownerPhone;
    private Long ownerId;
    private List<String> residents;
}
