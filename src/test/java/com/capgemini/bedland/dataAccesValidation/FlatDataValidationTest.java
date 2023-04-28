package com.capgemini.bedland.dataAccesValidation;

import com.capgemini.bedland.dataPreparation.EntityPreparator;
import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
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
class FlatDataValidationTest {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    @Autowired
    private FlatRepository flatRepository;

    private String createToLongString(int stringLenght) {
        String s = "a";
        StringBuilder stringBuilder = new StringBuilder();
        while (s.length() < stringLenght) {
            s = stringBuilder.append(s).toString();
        }
        return s;
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenSavingFlatWithToLongNumber() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        String toLongString = createToLongString(10);
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        FlatEntity flatEntity = EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1);
        flatEntity.setNumber(toLongString);
        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> flatRepository.save(flatEntity));
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionWhenSavingFlatWithToLongShapePath() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        String toLongString = createToLongString(250);
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        FlatEntity flatEntity = EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1);
        flatEntity.setShapePath(toLongString);
        //when
        //then
        assertThrows(DataIntegrityViolationException.class, () -> flatRepository.save(flatEntity));
    }
}
