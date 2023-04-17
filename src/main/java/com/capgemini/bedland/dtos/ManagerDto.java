package com.capgemini.bedland.dtos;

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
