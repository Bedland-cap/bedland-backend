package com.capgemini.bedland.building.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class BuildingDto extends AbstractDto {

    private Long managerId;
    private String buildingName;
    private String address;
    private int floors;
    private byte[] photo;
}
