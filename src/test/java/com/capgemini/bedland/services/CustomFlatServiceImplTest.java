package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.FlatShortenDetailsDto;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.repositories.*;
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
class CustomFlatServiceImplTest {

    @Autowired
    private CustomFlatService customFlatService;
    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BuildingRepository buildingRepository;
    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private PaymentRepository paymentRepository;


    @Test
    void shouldOwnersFlats_whenGettingFlatsForGivenOwner() {
        //given
        OwnerEntity exampleOwner = ownerRepository.findAll().get(0);
        List<FlatShortenDetailsDto> expectedFlatsForGivenOwner = new ArrayList<>();
        exampleOwner
                .getFlatEntities()
                .forEach(flat -> expectedFlatsForGivenOwner
                        .add(FlatShortenDetailsDto
                                .builder()
                                .flatNumber(flat.getNumber())
                                .flatAddress(flat.getBuildingEntity().getAddress())
                                .flatId(flat.getId())
                                .build()));
        //when
        List<FlatShortenDetailsDto> foundFlatsForGivenOwner = customFlatService.getFlatsForGivenOwner(exampleOwner.getId());
        //then
        assertEquals(expectedFlatsForGivenOwner, foundFlatsForGivenOwner);
        assertFalse(foundFlatsForGivenOwner.isEmpty());
        assertFalse(expectedFlatsForGivenOwner.isEmpty());
    }

    @Test
    void shouldThrowIllegalArgumentException_whenGettingFlatsForGivenOwner_andOwnerIsNull() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customFlatService.getFlatsForGivenOwner(null));

    }

    @Test
    void shouldThrowIllegalArgumentException_whenGettingFlatsForGivenOwner_andOwnerIsNotInDB() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customFlatService.getFlatsForGivenOwner(9999999L));

    }

    @Test
    void shouldGetFlatDetailsForGivenManagerInGivenBuilding_whenGettingFlatDetailsForGivenManagerInGivenBuilding() {
        //given
//        ManagerEntity manager = managerRepository.save(EntityPreparator.prepareFirstTestManager());
//        BuildingEntity building = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(manager));
//        OwnerEntity owner = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
//        FlatEntity flatEntity = EntityPreparator.prepareFirstTestFlat(building);
//        flatEntity.setFlatOwnerEntity(owner);
//        FlatEntity flat = flatRepository.save(flatEntity);
//
//        //when
//        List<FlatDetailsDto> flatDetailsForGivenManagerInGivenBuilding = customFlatService.getFlatDetailsForGivenManagerInGivenBuilding(manager.getId(), building.getId());
//        //then
        assertEquals(4, customFlatService.getFlatDetailsForGivenManagerInGivenBuilding(1L, 1L).size());
    }
}
