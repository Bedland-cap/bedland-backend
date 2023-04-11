package com.capgemini.bedland.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ContactSummaryDto {

private Long flatId;
private Long residentId;
private String residentName;
private String residentLastName;
}
