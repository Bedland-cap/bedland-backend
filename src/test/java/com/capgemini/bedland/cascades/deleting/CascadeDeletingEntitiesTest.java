

package com.capgemini.bedland.cascades.deleting;

import com.capgemini.bedland.dataPreparation.EntityPreparator;
import com.capgemini.bedland.entities.*;
import com.capgemini.bedland.repositories.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class CascadeDeletingEntitiesTest {

    @Autowired
    ManagerRepository managerRepository;

    @Autowired
    BuildingRepository buildingRepository;

    @Autowired
    FlatRepository flatRepository;

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OwnerRepository ownerRepository;

    @AfterEach
    void cleanDB() {
        memberRepository.deleteAll();
        flatRepository.deleteAll();
        buildingRepository.deleteAll();
        managerRepository.deleteAll();
        ownerRepository.deleteAll();
    }

    @Test
    void shouldDeleteBuildingWhenDeletingManager() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity1 = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        BuildingEntity buildingEntity2 = buildingRepository.save(EntityPreparator.prepareSecondTestBuilding(managerEntity));

        List<ManagerEntity> allManagersBeforeDeletingOne = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        //when
        managerRepository.deleteById(managerEntity.getId());
        List<BuildingEntity> allBuildingsAfterDeletingManager = buildingRepository.findAll();
        List<ManagerEntity> allManagersAfterDeletingOne = managerRepository.findAll();
        //then
        //before deleting
        assertFalse(allManagersBeforeDeletingOne.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(2, allBuildingsBeforeDeletingManager.size());
        assertEquals(1, allManagersBeforeDeletingOne.size());

        assertAll("Manager equality assertion",
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getId(), managerEntity.getId()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getName(), managerEntity.getName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getLastName(), managerEntity.getLastName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getEmail(), managerEntity.getEmail())
        );
        assertAll("Buildings equality assertion",
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getId(), buildingEntity1.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getBuildingName(), buildingEntity1.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getAddress(), buildingEntity1.getAddress()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(1).getId(), buildingEntity2.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(1).getBuildingName(), buildingEntity2.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(1).getAddress(), buildingEntity2.getAddress())
        );

        //after deleting
        assertFalse(managerRepository.existsById(managerEntity.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity1.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity2.getId()));

        assertTrue(allBuildingsAfterDeletingManager.isEmpty());
        assertTrue(allManagersAfterDeletingOne.isEmpty());
    }

    @Test
    void shouldDeleteFlatsAndBuildingWhenDeletingManager() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));

        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        OwnerEntity ownerEntity2 = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());

                FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1));
        FlatEntity flatEntity2 = flatRepository.save(EntityPreparator.prepareSecondTestFlat(buildingEntity,ownerEntity2));

        List<ManagerEntity> allManagersBeforeDeletingOne = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        List<FlatEntity> allFlatsBeforeDeletingManager = flatRepository.findAll();
        //when
        managerRepository.deleteById(managerEntity.getId());
        List<FlatEntity> allFlatsAfterDeletingManager = flatRepository.findAll();
        List<BuildingEntity> allBuildingsAfterDeletingManager = buildingRepository.findAll();
        List<ManagerEntity> allManagersAfterDeletingOne = managerRepository.findAll();
        //then
        //before deleting
        assertFalse(allFlatsBeforeDeletingManager.isEmpty());
        assertFalse(allManagersBeforeDeletingOne.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(2, allFlatsBeforeDeletingManager.size());
        assertEquals(1, allBuildingsBeforeDeletingManager.size());
        assertEquals(1, allManagersBeforeDeletingOne.size());

        assertAll("Manager equality assertion",
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getId(), managerEntity.getId()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getName(), managerEntity.getName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getLastName(), managerEntity.getLastName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getEmail(), managerEntity.getEmail())
        );

        assertAll("Building equality assertion",
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getId(), buildingEntity.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getBuildingName(), buildingEntity.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getFloors(), buildingEntity.getFloors()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getAddress(), buildingEntity.getAddress())
        );

        assertAll("Flats equality assertion",
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getId(), flatEntity1.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getNumber(), flatEntity1.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getFloor(), flatEntity1.getFloor()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getId(), flatEntity2.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getNumber(), flatEntity2.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getFloor(), flatEntity2.getFloor())
        );
        //after deleting
        assertFalse(managerRepository.existsById(managerEntity.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity.getId()));
        assertFalse(flatRepository.existsById(flatEntity1.getId()));
        assertFalse(flatRepository.existsById(flatEntity2.getId()));

        assertTrue(allFlatsAfterDeletingManager.isEmpty());
        assertTrue(allBuildingsAfterDeletingManager.isEmpty());
        assertTrue(allManagersAfterDeletingOne.isEmpty());
    }

    @Test
    void shouldDeleteFlatsWhenDeletingBuilding() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));

        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        OwnerEntity ownerEntity2 = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());

        FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1));
        FlatEntity flatEntity2 = flatRepository.save(EntityPreparator.prepareSecondTestFlat(buildingEntity,ownerEntity2));

        List<ManagerEntity> allMenagers = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        List<FlatEntity> allFlatsBeforeDeletingManager = flatRepository.findAll();
        //when
        buildingRepository.deleteById(buildingEntity.getId());
        List<FlatEntity> allFlatsAfterDeletingManager = flatRepository.findAll();
        List<BuildingEntity> allBuildingsAfterDeletingManager = buildingRepository.findAll();
        //then
        //before deleting
        assertFalse(allFlatsBeforeDeletingManager.isEmpty());
        assertFalse(allMenagers.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(2, allFlatsBeforeDeletingManager.size());
        assertEquals(1, allBuildingsBeforeDeletingManager.size());
        assertEquals(1, allMenagers.size());

        assertAll("Building equality assertion",
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getId(), buildingEntity.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getBuildingName(), buildingEntity.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getFloors(), buildingEntity.getFloors()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getAddress(), buildingEntity.getAddress())
        );

        assertAll("Flats equality assertion",
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getId(), flatEntity1.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getNumber(), flatEntity1.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getFloor(), flatEntity1.getFloor()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getId(), flatEntity2.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getNumber(), flatEntity2.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getFloor(), flatEntity2.getFloor())
        );
        //after deleting
        assertTrue(managerRepository.existsById(managerEntity.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity.getId()));
        assertFalse(flatRepository.existsById(flatEntity1.getId()));
        assertFalse(flatRepository.existsById(flatEntity2.getId()));
        assertTrue(allFlatsAfterDeletingManager.isEmpty());
        assertTrue(allBuildingsAfterDeletingManager.isEmpty());
    }

    @Test
    void shouldDeleteMembersFlatsBuildingAndManagerWhenDeletingManager() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        OwnerEntity ownerEntity2 = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());
        FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1));
        FlatEntity flatEntity2 = flatRepository.save(EntityPreparator.prepareSecondTestFlat(buildingEntity,ownerEntity2));
        MemberEntity memberEntity1 = memberRepository.save(EntityPreparator.prepareFirstTestMember_Owner(flatEntity1));
        MemberEntity memberEntity2 = memberRepository.save(EntityPreparator.prepareFirstTestMember(flatEntity1));
        MemberEntity memberEntity3 = memberRepository.save(EntityPreparator.prepareSecondTestMember_Owner(flatEntity2));
        MemberEntity memberEntity4 = memberRepository.save(EntityPreparator.prepareSecondTestMember(flatEntity2));

        List<ManagerEntity> allManagersBeforeDeletingOne = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        List<FlatEntity> allFlatsBeforeDeletingManager = flatRepository.findAll();
        List<MemberEntity> allMembersBeforeDeletingManager = memberRepository.findAll();
        //when
        managerRepository.deleteById(managerEntity.getId());
        List<MemberEntity> allMembersAfterDeletingManager = memberRepository.findAll();
        List<FlatEntity> allFlatsAfterDeletingManager = flatRepository.findAll();
        List<BuildingEntity> allBuildingsAfterDeletingManager = buildingRepository.findAll();
        List<ManagerEntity> allManagersAfterDeletingOne = managerRepository.findAll();
        //then
        //before deleting
        assertFalse(allMembersBeforeDeletingManager.isEmpty());
        assertFalse(allFlatsBeforeDeletingManager.isEmpty());
        assertFalse(allManagersBeforeDeletingOne.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(4, allMembersBeforeDeletingManager.size());
        assertEquals(2, allFlatsBeforeDeletingManager.size());
        assertEquals(1, allBuildingsBeforeDeletingManager.size());
        assertEquals(1, allManagersBeforeDeletingOne.size());

        assertAll("Manager equality assertion",
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getId(), managerEntity.getId()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getName(), managerEntity.getName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getLastName(), managerEntity.getLastName()),
                () -> assertEquals(allManagersBeforeDeletingOne.get(0).getEmail(), managerEntity.getEmail())
        );

        assertAll("Building equality assertion",
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getId(), buildingEntity.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getBuildingName(), buildingEntity.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getFloors(), buildingEntity.getFloors()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getAddress(), buildingEntity.getAddress())
        );

        assertAll("Flats equality assertion",
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getId(), flatEntity1.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getNumber(), flatEntity1.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getFloor(), flatEntity1.getFloor()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getId(), flatEntity2.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getNumber(), flatEntity2.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getFloor(), flatEntity2.getFloor())
        );
        assertAll("Members equality assertion",
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getId(), memberEntity1.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getName(), memberEntity1.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getLastName(), memberEntity1.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getEmail(), memberEntity1.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getId(), memberEntity2.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getName(), memberEntity2.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getLastName(), memberEntity2.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getEmail(), memberEntity2.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getId(), memberEntity3.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getName(), memberEntity3.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getLastName(), memberEntity3.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getEmail(), memberEntity3.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getId(), memberEntity4.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getName(), memberEntity4.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getLastName(), memberEntity4.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getEmail(), memberEntity4.getEmail())
        );

        //after deleting
        assertFalse(managerRepository.existsById(managerEntity.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity.getId()));
        assertFalse(flatRepository.existsById(flatEntity1.getId()));
        assertFalse(flatRepository.existsById(flatEntity2.getId()));
        assertFalse(memberRepository.existsById(memberEntity1.getId()));
        assertFalse(memberRepository.existsById(memberEntity2.getId()));
        assertFalse(memberRepository.existsById(memberEntity3.getId()));
        assertFalse(memberRepository.existsById(memberEntity4.getId()));

        assertTrue(allMembersAfterDeletingManager.isEmpty());
        assertTrue(allFlatsAfterDeletingManager.isEmpty());
        assertTrue(allBuildingsAfterDeletingManager.isEmpty());
        assertTrue(allManagersAfterDeletingOne.isEmpty());
    }

    @Test
    void shouldDeleteMembersFlatsAndBuildingWhenDeletingBuilding() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        OwnerEntity ownerEntity2 = ownerRepository.save(EntityPreparator.prepareSecondTestOwner());
        FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1));
        FlatEntity flatEntity2 = flatRepository.save(EntityPreparator.prepareSecondTestFlat(buildingEntity,ownerEntity2));
        MemberEntity memberEntity1 = memberRepository.save(EntityPreparator.prepareFirstTestMember_Owner(flatEntity1));
        MemberEntity memberEntity2 = memberRepository.save(EntityPreparator.prepareFirstTestMember(flatEntity1));
        MemberEntity memberEntity3 = memberRepository.save(EntityPreparator.prepareSecondTestMember_Owner(flatEntity2));
        MemberEntity memberEntity4 = memberRepository.save(EntityPreparator.prepareSecondTestMember(flatEntity2));

        List<ManagerEntity> allManagersBeforeDeletingOne = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        List<FlatEntity> allFlatsBeforeDeletingManager = flatRepository.findAll();
        List<MemberEntity> allMembersBeforeDeletingManager = memberRepository.findAll();
        //when
        buildingRepository.deleteById(buildingEntity.getId());
        List<MemberEntity> allMembersAfterDeletingManager = memberRepository.findAll();
        List<FlatEntity> allFlatsAfterDeletingManager = flatRepository.findAll();
        List<BuildingEntity> allBuildingsAfterDeletingManager = buildingRepository.findAll();
        //then
        //before deleting
        assertFalse(allMembersBeforeDeletingManager.isEmpty());
        assertFalse(allFlatsBeforeDeletingManager.isEmpty());
        assertFalse(allManagersBeforeDeletingOne.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(4, allMembersBeforeDeletingManager.size());
        assertEquals(2, allFlatsBeforeDeletingManager.size());
        assertEquals(1, allBuildingsBeforeDeletingManager.size());

        assertAll("Building equality assertion",
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getId(), buildingEntity.getId()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getBuildingName(), buildingEntity.getBuildingName()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getFloors(), buildingEntity.getFloors()),
                () -> assertEquals(allBuildingsBeforeDeletingManager.get(0).getAddress(), buildingEntity.getAddress())
        );

        assertAll("Flats equality assertion",
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getId(), flatEntity1.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getNumber(), flatEntity1.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getFloor(), flatEntity1.getFloor()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getId(), flatEntity2.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getNumber(), flatEntity2.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(1).getFloor(), flatEntity2.getFloor())
        );
        assertAll("Members equality assertion",
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getId(), memberEntity1.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getName(), memberEntity1.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getLastName(), memberEntity1.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getEmail(), memberEntity1.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getId(), memberEntity2.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getName(), memberEntity2.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getLastName(), memberEntity2.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getEmail(), memberEntity2.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getId(), memberEntity3.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getName(), memberEntity3.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getLastName(), memberEntity3.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(2).getEmail(), memberEntity3.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getId(), memberEntity4.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getName(), memberEntity4.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getLastName(), memberEntity4.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(3).getEmail(), memberEntity4.getEmail())
        );

        //after deleting
        assertTrue(managerRepository.existsById(managerEntity.getId()));
        assertFalse(buildingRepository.existsById(buildingEntity.getId()));
        assertFalse(flatRepository.existsById(flatEntity1.getId()));
        assertFalse(flatRepository.existsById(flatEntity2.getId()));
        assertFalse(memberRepository.existsById(memberEntity1.getId()));
        assertFalse(memberRepository.existsById(memberEntity2.getId()));
        assertFalse(memberRepository.existsById(memberEntity3.getId()));
        assertFalse(memberRepository.existsById(memberEntity4.getId()));

        assertTrue(allMembersAfterDeletingManager.isEmpty());
        assertTrue(allFlatsAfterDeletingManager.isEmpty());
        assertTrue(allBuildingsAfterDeletingManager.isEmpty());
    }

    @Test
    void shouldDeleteMembersAndFlatWhenDeletingFlat() {
        //given
        ManagerEntity managerEntity = managerRepository.save(EntityPreparator.prepareFirstTestManager());
        BuildingEntity buildingEntity = buildingRepository.save(EntityPreparator.prepareFirstTestBuilding(managerEntity));
        OwnerEntity ownerEntity1 = ownerRepository.save(EntityPreparator.prepareFirstTestOwner());
        FlatEntity flatEntity1 = flatRepository.save(EntityPreparator.prepareFirstTestFlat(buildingEntity,ownerEntity1));
        MemberEntity memberEntity1 = memberRepository.save(EntityPreparator.prepareFirstTestMember_Owner(flatEntity1));
        MemberEntity memberEntity2 = memberRepository.save(EntityPreparator.prepareFirstTestMember(flatEntity1));

        List<ManagerEntity> allManagersBeforeDeletingOne = managerRepository.findAll();
        List<BuildingEntity> allBuildingsBeforeDeletingManager = buildingRepository.findAll();
        List<FlatEntity> allFlatsBeforeDeletingManager = flatRepository.findAll();
        List<MemberEntity> allMembersBeforeDeletingManager = memberRepository.findAll();
        //when
        flatRepository.deleteById(flatEntity1.getId());
        List<MemberEntity> allMembersAfterDeletingManager = memberRepository.findAll();
        List<FlatEntity> allFlatsAfterDeletingManager = flatRepository.findAll();
        //then
        //before deleting
        assertFalse(allMembersBeforeDeletingManager.isEmpty());
        assertFalse(allFlatsBeforeDeletingManager.isEmpty());
        assertFalse(allManagersBeforeDeletingOne.isEmpty());
        assertFalse(allBuildingsBeforeDeletingManager.isEmpty());
        assertEquals(2, allMembersBeforeDeletingManager.size());
        assertEquals(1, allFlatsBeforeDeletingManager.size());
        assertEquals(1, allBuildingsBeforeDeletingManager.size());

        assertAll("Flats equality assertion",
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getId(), flatEntity1.getId()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getNumber(), flatEntity1.getNumber()),
                () -> assertEquals(allFlatsBeforeDeletingManager.get(0).getFloor(), flatEntity1.getFloor())
        );
        assertAll("Members equality assertion",
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getId(), memberEntity1.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getName(), memberEntity1.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getLastName(), memberEntity1.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(0).getEmail(), memberEntity1.getEmail()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getId(), memberEntity2.getId()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getName(), memberEntity2.getName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getLastName(), memberEntity2.getLastName()),
                () -> assertEquals(allMembersBeforeDeletingManager.get(1).getEmail(), memberEntity2.getEmail())
        );

        //after deleting
        assertTrue(managerRepository.existsById(managerEntity.getId()));
        assertTrue(buildingRepository.existsById(buildingEntity.getId()));
        assertFalse(flatRepository.existsById(flatEntity1.getId()));
        assertFalse(memberRepository.existsById(memberEntity1.getId()));
        assertFalse(memberRepository.existsById(memberEntity2.getId()));

        assertTrue(allMembersAfterDeletingManager.isEmpty());
        assertTrue(allFlatsAfterDeletingManager.isEmpty());
    }

}
