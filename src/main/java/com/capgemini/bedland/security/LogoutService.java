package com.capgemini.bedland.security;

import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final UserDao userDao;
    private final OwnerRepository ownerRepository;
    private final ManagerRepository managerRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        Optional<OwnerEntity> owner = ownerRepository.findByToken(jwt);
        Optional<ManagerEntity> manager = managerRepository.findByToken(jwt);
        owner.ifPresent(ownerEntity -> userDao.deleteTokenInDB(ownerEntity
                                                                       .getId(), "ROLE_USER"));
        manager.ifPresent(managerEntity -> userDao.deleteTokenInDB(managerEntity
                                                                           .getId(), "ROLE_MANAGER"));
    }
}