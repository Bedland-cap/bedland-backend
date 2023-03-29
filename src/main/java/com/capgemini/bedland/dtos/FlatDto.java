package com.capgemini.bedland.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class FlatDto extends AbstractDto {

    private Long buildingId;
    private String number;
    private int floor;
    private String shapePath;

}
