package com.capgemini.bedland.incident_status.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
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
    private String incidentStatusName;

}
