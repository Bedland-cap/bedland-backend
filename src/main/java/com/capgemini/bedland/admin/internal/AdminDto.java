package com.capgemini.bedland.admin.internal;

import com.capgemini.bedland.abstract_entity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AdminDto extends AbstractDto {

    private String login;
    private String password;

}
