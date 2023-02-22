package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.abstractEntity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class BuildingDto extends AbstractDto {

    private Long managerId;
    private String buildingName;
    private String address;
    private int floors;
}
