package com.capgemini.bedland.dtos;

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