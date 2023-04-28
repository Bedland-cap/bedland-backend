package com.capgemini.bedland.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogoutRequest {

    private Long userId;
    private String userRole;

}