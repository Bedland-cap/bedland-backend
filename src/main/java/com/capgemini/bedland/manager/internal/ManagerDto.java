package com.capgemini.bedland.manager.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class ManagerDto extends AbstractDto {
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private byte[] avatar;

}
