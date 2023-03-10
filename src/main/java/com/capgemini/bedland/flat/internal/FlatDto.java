package com.capgemini.bedland.flat.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
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
