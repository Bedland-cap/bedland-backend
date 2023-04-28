package com.capgemini.bedland.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class OwnerDto  extends AbstractDto{
    private String login;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Long flatId;
    private byte[] avatar;
}
