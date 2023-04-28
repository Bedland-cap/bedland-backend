package com.capgemini.bedland.dataAccesValidation;

import com.capgemini.bedland.dataPreparation.EntityPreparator;
import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase
@SpringBootTest
class BuildingDataValidationTest {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ManagerRepository managerRepository;

    private String createToLongString(int stringLenght) {
        String s = "a";
        StringBuilder stringBuilder = new StringBuilder();
        while (s.length() < stringLenght) {
            s = stringBuilder.append(s).toString();
        }
        return s;
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenSavingBuildingWithToLongName() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        String toLongName = createToLongString(55);
        BuildingEntity buildingEntity = EntityPreparator.prepareFirstTestBuilding(managerEntity);
        buildingEntity.setBuildingName(toLongName);
        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> buildingRepository.save(buildingEntity));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenSavingBuildingWithToLongAddress() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        String toLongName = createToLongString(80);
        BuildingEntity buildingEntity = EntityPreparator.prepareFirstTestBuilding(managerEntity);
        buildingEntity.setAddress(toLongName);
        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> buildingRepository.save(buildingEntity));
    }

}
