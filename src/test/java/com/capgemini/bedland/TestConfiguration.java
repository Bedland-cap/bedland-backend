package com.capgemini.bedland;

import com.capgemini.bedland.security.AuthenticationController;
import com.capgemini.bedland.security.JwtAuthFilter;
import com.capgemini.bedland.security.JwtUtils;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfiguration {
    @MockBean
    private AuthenticationController authenticationControllerMock;
    @MockBean
    private JwtAuthFilter jwtAuthFilter;
    @MockBean
    private JwtUtils jwtUtils;

}
