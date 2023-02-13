package com.capgemini.bedland.manager.internal;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ManagerDto {

    private Long id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;

    // TODO: add buildings to DTO when buildings mapper will be implemented
    // private List<BuildingDto> managedBuildings;

}
