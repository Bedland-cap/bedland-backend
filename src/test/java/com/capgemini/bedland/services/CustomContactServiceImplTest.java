package com.capgemini.bedland.services;

import com.capgemini.bedland.repositories.ManagerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
@Transactional
public class CustomContactServiceImplTest {

    @Autowired
    private CustomContactService customContactService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private CustomFlatService customFlatService;
    @Autowired
    private CustomNotificationService customNotificationService;

    //todo: on monday
    @Test
    void shouldGetContactsForGivenManager() {
        //given
//        List<ContactSummaryDto> expectedContacts = new ArrayList<>();
//        ManagerEntity exampleManager = managerRepository.findAll().get(0);

        //when

        //then

    }
}
