package com.capgemini.bedland.manager.internal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
public class ManagerServiceImplTest {

    @Autowired
    private ManagerServiceImpl managerService;

    @Test
    void chechIfFirstManagerExist() {
        System.out.println(managerService.getById(1L));
    }

    @Test
    void chechIfSecondManagerExist() {
        System.out.println(managerService.getById(2L));
    }

}