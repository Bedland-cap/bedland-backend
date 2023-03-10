package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ManagerDto extends AbstractDto {

    private Long id;
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;

}
