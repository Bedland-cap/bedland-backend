package com.capgemini.bedland.incident.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class IncidentDto extends AbstractDto {

    private Long flatId;
    private String title;
    private String description;
    private boolean commonSpace;

}