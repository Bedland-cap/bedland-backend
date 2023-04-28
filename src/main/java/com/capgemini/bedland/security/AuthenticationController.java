package com.capgemini.bedland.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserDao userDao;
    private final JwtUtils jwtUtils;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getLogin(), request.getPassword())
        );
        final UserDetails user = userDao.findUserByLogin(request.getLogin());
        if (user != null) {
            AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwtUtils.generateToken(user),
                                                                                       userDao.findOwnerOrManagerByLogin(
                                                                                               request.getLogin()),
                                                                                       userDao.findRoleByUser(user));
            return ResponseEntity.ok(authenticationResponse);
        }
        throw new IllegalArgumentException("User is null! Some error has occurred");
    }

}
