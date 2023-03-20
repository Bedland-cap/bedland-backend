package com.capgemini.bedland.member.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
public class MemberDto extends AbstractDto {

    private String login;
    private String password;
    private String name;
    private String lastName;
    private String email;
    private String phoneNumber;
    private boolean isOwner;
    private Long flatId;
    private byte[] avatar;

}
