package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.BuildingDto;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.BuildingMapper;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureTestDatabase
@SpringBootTest
class CustomBuildingServiceImplTest {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private CustomBuildingService customBuildingService;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BuildingMapper buildingMapper;

    @Test
    void shouldGetBuildingsByWhenGettingBuildingsByManager() {
        //given
        Long id = managerRepository.findAll().get(0).getId();
        List<BuildingDto> expectedBuildings = buildingMapper.entities2DTOs(buildingRepository.findAll().stream().filter(b -> b.getManagerEntity().getId().equals(id)).toList());
        //when
        List<BuildingDto> buildingsByManager = customBuildingService.getBuildingsByManager(id);
        //then
        assertNotEquals(null, id);
        assertEquals(expectedBuildings, buildingsByManager);
    }
    @Test
    void shouldThrowNotFoundExceptionWhenFindingBuildingByIdAndBuildingIsNotPresentInDB() {
        //given
        Long sampleID = 999999L;
        //when
        //then
        assertThrows(NotFoundException.class, () -> customBuildingService.getBuildingsByManager(sampleID));
    }
    @Test
    void shouldThrowIllegalArgumentExceptionWhenFindingBuildingByIdAndIDIsNull() {
        //given
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> customBuildingService.getBuildingsByManager(null));
    }
}
