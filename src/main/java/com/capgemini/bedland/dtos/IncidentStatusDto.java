package com.capgemini.bedland.dtos;

import com.capgemini.bedland.enums.IncidentStatusName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class IncidentStatusDto extends AbstractDto {

    private Long incidentId;
    private IncidentStatusName incidentStatusName;

}
