package com.capgemini.bedland.security;

import com.capgemini.bedland.manager.internal.ManagerRepository;
import com.capgemini.bedland.member.internal.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDao {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    private List<UserDetails> createUserList() {
        List<UserDetails> usersList = new ArrayList<>(List.of(
                User.withUsername("user")
                    .password(passwordEncoder.encode("user"))
                    .roles("USER")
                    .build(),
                User.withUsername("admin")
                    .password(passwordEncoder.encode("admin"))
                    .roles("ADMIN")
                    .build(),
                User.withUsername("manager")
                    .password(passwordEncoder.encode("manager"))
                    .roles("MANAGER")
                    .build())
        );
        memberRepository.findAll()
                        .forEach(member -> usersList.add(User.withUsername(member.getLogin())
                                                             .password(passwordEncoder.encode(member.getPassword()))
                                                             .roles("USER")
                                                             .build()));
        managerRepository.findAll()
                         .forEach(member -> usersList.add(User.withUsername(member.getLogin())
                                                              .password(passwordEncoder.encode(member.getPassword()))
                                                              .roles("MANAGER")
                                                              .build()));
        return usersList;
    }

    public UserDetails findUserByLogin(String login) {
        return createUserList().stream()
                               .filter(user -> user.getUsername()
                                                   .equals(login))
                               .findFirst()
                               .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

}
