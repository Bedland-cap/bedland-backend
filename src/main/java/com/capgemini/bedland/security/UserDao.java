package com.capgemini.bedland.security;

import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.MemberEntity;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserDao {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    private List<UserDetails> createUserList(String login) {
        Optional<MemberEntity> member = memberRepository.findAll()
                                                        .stream()
                                                        .filter(user -> user.getLogin()
                                                                  .equals(login)).findFirst();
        Optional<ManagerEntity> manager = managerRepository.findAll()
                                                 .stream()
                                                 .filter(user -> user.getLogin()
                                                                  .equals(login)).findFirst();
        List<UserDetails> usersList = new ArrayList<>();
        if(member.isPresent()){
            usersList.add(User.withUsername(member.get().getLogin())
                              .password(passwordEncoder.encode(member.get().getPassword()))
                              .roles("USER")
                              .build());
        }
        if(manager.isPresent()){
            usersList.add(User.withUsername(manager.get().getLogin())
                              .password(passwordEncoder.encode(manager.get().getPassword()))
                              .roles("MANAGER")
                              .build());
        }
        usersList.add(User.withUsername("admin")
                          .password(passwordEncoder.encode("admin"))
                          .roles("ADMIN")
                          .build());
        return usersList;
    }

    public UserDetails findUserByLogin(String login) {
        return createUserList(login).stream().findFirst()
                                    .orElseThrow(() -> new UsernameNotFoundException("No user was found"));
    }

}
