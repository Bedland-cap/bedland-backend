package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.abstractEntity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class FlatDto extends AbstractDto {

    private Long buildingId;
    private String number;
    private int floor;
    private String shapePath;

}
