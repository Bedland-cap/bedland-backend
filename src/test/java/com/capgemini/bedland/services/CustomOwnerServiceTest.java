package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.OwnerSummaryDto;
import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
class CustomOwnerServiceTest {

    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private CustomOwnerService customOwnerService;

    @Test
    void shouldReturnOwnersListFromManagerWhenGettingManagerId() {
        //given
        ManagerEntity exampleManager = managerRepository.findAll()
                                                        .get(0);
        List<OwnerSummaryDto> expectedOwnersForGivenManager = new ArrayList<>();
        exampleManager.getBuildingEntities()
                      .forEach(building -> {
                          building.getFlatEntities().forEach(flat -> {
                              expectedOwnersForGivenManager.add(OwnerSummaryDto.builder()
                                                                               .buildingName(building.getBuildingName())
                                                                               .flatNumber(flat.getNumber())
                                                                               .flatFloor(flat.getFloor())
                                                                               .flatId(flat.getId())
                                                                               .ownerNameAndLastName(flat.getFlatOwnerEntity().getName() + " " + flat.getFlatOwnerEntity().getLastName())
                                                                               .ownerPhone(flat.getFlatOwnerEntity().getPhoneNumber())
                                                                               .ownerId(flat.getFlatOwnerEntity().getId())
                                                                               .build());
                          });
                      });
        //when
        List<OwnerSummaryDto> foundOwnerForGivenManager = customOwnerService.findAllOwnersForGivenManager(exampleManager.getId());
        //then
        assertEquals(expectedOwnersForGivenManager, foundOwnerForGivenManager);
        assertFalse(foundOwnerForGivenManager.isEmpty());
        assertFalse(expectedOwnersForGivenManager.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenWhenGettingManagerIdAndOwnerIsNull() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customOwnerService.findAllOwnersForGivenManager(null));

    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingFlatsForGivenOwner_andOwnerIsNotInDB() {
        //given
        //when
        //then
        assertThrows(NotFoundException.class, () -> customOwnerService.findAllOwnersForGivenManager(Long.MAX_VALUE));

    }

}