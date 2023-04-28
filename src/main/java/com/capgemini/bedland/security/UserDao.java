package com.capgemini.bedland.security;

import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDetails findUserByLogin(String login) {
        return createUserList(login).stream()
                                    .findFirst()
                                    .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

    public Long findOwnerOrManagerByLogin(String login) {
        Optional<OwnerEntity> owner = ownerRepository.findAll()
                                                     .stream()
                                                     .filter(user -> user.getLogin()
                                                                         .equals(login))
                                                     .findFirst();
        Optional<ManagerEntity> manager = managerRepository.findAll()
                                                           .stream()
                                                           .filter(user -> user.getLogin()
                                                                               .equals(login))
                                                           .findFirst();
        Long userId = 0L;
        if (manager.isPresent()) {
            userId = manager.get()
                            .getId();
        }
        if (owner.isPresent()) {
            userId = owner.get()
                          .getId();
        }

        return userId;
    }

    public String findRoleByUser(UserDetails user) {
        return user.getAuthorities()
                   .stream()
                   .findFirst()
                   .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                   .toString();
    }

    private List<UserDetails> createUserList(String login) {
        List<UserDetails> usersList = new ArrayList<>();
        Optional<OwnerEntity> owner = ownerRepository.findAll()
                                                     .stream()
                                                     .filter(user -> user.getLogin()
                                                                         .equals(login))
                                                     .findFirst();
        Optional<ManagerEntity> manager = managerRepository.findAll()
                                                           .stream()
                                                           .filter(user -> user.getLogin()
                                                                               .equals(login))
                                                           .findFirst();

        owner.ifPresent(ownerEntity -> usersList.add(User.withUsername(ownerEntity.getLogin())
                                                         .password(passwordEncoder.encode(ownerEntity.getPassword()))
                                                         .roles("USER")
                                                         .build()));

        manager.ifPresent(managerEntity -> usersList.add(User.withUsername(managerEntity.getLogin())
                                                             .password(passwordEncoder.encode(managerEntity.getPassword()))
                                                             .roles("MANAGER")
                                                             .build()));

        usersList.add(User.withUsername("admin")
                          .password(passwordEncoder.encode("admin"))
                          .roles("ADMIN")
                          .build());

        return usersList;
    }

    public void deleteTokenInDB(Long userId, String userRole) {
        if (Objects.equals(userRole, "ROLE_USER")) {
            Optional<OwnerEntity> owner = ownerRepository.findById(userId);
            owner.ifPresent(ownerEntity -> {
                ownerEntity.setToken(null);
                ownerRepository.save(ownerEntity);
            });
        }
        if (Objects.equals(userRole, "ROLE_MANAGER")) {
            Optional<ManagerEntity> manager = managerRepository.findById(userId);
            manager.ifPresent(managerEntity -> {
                managerEntity.setToken(null);
                managerRepository.save(managerEntity);
            });
        }
    }

}
