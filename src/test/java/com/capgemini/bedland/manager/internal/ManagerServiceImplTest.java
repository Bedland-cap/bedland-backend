package com.capgemini.bedland.manager.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ManagerServiceImplTest {

    @Autowired
    private ManagerServiceImpl managerService;

    @Test
    void should(){
        System.out.println(managerService.getById(1L));

    }

}