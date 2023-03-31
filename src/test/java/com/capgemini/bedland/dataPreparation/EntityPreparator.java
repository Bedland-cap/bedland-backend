package com.capgemini.bedland.dataPreparation;

import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.entities.ManagerEntity;
import com.capgemini.bedland.entities.MemberEntity;

public abstract class EntityPreparator {

    public static ManagerEntity prepareFirstTestManager() {
        ManagerEntity testManager1 = new ManagerEntity();
        testManager1.setName("managerNameTest");
        testManager1.setLastName("managerLastNameTest");
        testManager1.setPhoneNumber("111111");
        testManager1.setEmail("test123@mail");
        testManager1.setLogin("testLogin");
        testManager1.setPassword("testPassword");
        return testManager1;
    }

    public static BuildingEntity prepareFirstTestBuilding(ManagerEntity managerEntity) {
        BuildingEntity testBuilding1 = new BuildingEntity();
        testBuilding1.setManagerEntity(managerEntity);
        testBuilding1.setAddress("testAddress1");
        testBuilding1.setBuildingName("testBuildingName1");
        testBuilding1.setFloors(5);
        return testBuilding1;
    }

    public static BuildingEntity prepareSecondTestBuilding(ManagerEntity managerEntity) {
        BuildingEntity testBuilding2 = new BuildingEntity();
        testBuilding2.setManagerEntity(managerEntity);
        testBuilding2.setAddress("testAddress2");
        testBuilding2.setBuildingName("testBuildingName2");
        testBuilding2.setFloors(7);
        return testBuilding2;
    }

    public static BuildingEntity prepareThirdTestBuilding(ManagerEntity managerEntity) {
        BuildingEntity testBuilding3 = new BuildingEntity();
        testBuilding3.setManagerEntity(managerEntity);
        testBuilding3.setAddress("testAddress3");
        testBuilding3.setBuildingName("testBuildingName3");
        testBuilding3.setFloors(3);
        return testBuilding3;
    }

    public static FlatEntity prepareFirstTestFlat(BuildingEntity buildingEntity) {
        FlatEntity testFlat1 = new FlatEntity();
        testFlat1.setBuildingEntity(buildingEntity);
        testFlat1.setFloor(1);
        testFlat1.setShapePath("shapePath1");
        testFlat1.setNumber("1");
        return testFlat1;
    }

    public static FlatEntity prepareSecondTestFlat(BuildingEntity buildingEntity) {
        FlatEntity testFlat2 = new FlatEntity();
        testFlat2.setBuildingEntity(buildingEntity);
        testFlat2.setFloor(2);
        testFlat2.setShapePath("shapePath2");
        testFlat2.setNumber("2");
        return testFlat2;
    }

    public static FlatEntity prepareThirdTestFlat(BuildingEntity buildingEntity) {
        FlatEntity testFlat3 = new FlatEntity();
        testFlat3.setBuildingEntity(buildingEntity);
        testFlat3.setFloor(3);
        testFlat3.setShapePath("shapePath3");
        testFlat3.setNumber("3");
        return testFlat3;
    }

    public static FlatEntity prepareFourthTestFlat(BuildingEntity buildingEntity) {
        FlatEntity testFlat4 = new FlatEntity();
        testFlat4.setBuildingEntity(buildingEntity);
        testFlat4.setFloor(1);
        testFlat4.setShapePath("shapePath4");
        testFlat4.setNumber("4");
        return testFlat4;
    }

    public static MemberEntity prepareFirstTestMember_Owner(FlatEntity flatEntity) {
        MemberEntity memberEntity1 = new MemberEntity();
        memberEntity1.setFlatEntity(flatEntity);
        memberEntity1.setName("testName1");
        memberEntity1.setLastName("testLastName1");
        memberEntity1.setEmail("test1@mail");
        memberEntity1.setOwner(true);
        memberEntity1.setLogin("dadsadas");
        memberEntity1.setPassword("dasdadsad");
        memberEntity1.setPhoneNumber("4314123");
        return memberEntity1;
    }
    public static MemberEntity prepareSecondTestMember_Owner(FlatEntity flatEntity) {
        MemberEntity memberEntity5 = new MemberEntity();
        memberEntity5.setFlatEntity(flatEntity);
        memberEntity5.setName("owner2");
        memberEntity5.setLastName("owner2");
        memberEntity5.setEmail("owner2@mail");
        memberEntity5.setOwner(true);
        memberEntity5.setLogin("owner2");
        memberEntity5.setPassword("dasdadsad");
        memberEntity5.setPhoneNumber("4314123");
        return memberEntity5;
    }

    public static MemberEntity prepareFirstTestMember(FlatEntity flatEntity) {
        MemberEntity memberEntity2 = new MemberEntity();
        memberEntity2.setFlatEntity(flatEntity);
        memberEntity2.setName("testName11");
        memberEntity2.setLastName("testLastName11");
        memberEntity2.setEmail("test11@mail");
        memberEntity2.setOwner(false);
        memberEntity2.setLogin("dadsadasa");
        memberEntity2.setPassword("dasdadsdaad");
        memberEntity2.setPhoneNumber("43124123");
        return memberEntity2;
    }

    public static MemberEntity prepareSecondTestMember(FlatEntity flatEntity) {
        MemberEntity memberEntity3 = new MemberEntity();
        memberEntity3.setFlatEntity(flatEntity);
        memberEntity3.setName("testName2");
        memberEntity3.setLastName("testLastName2");
        memberEntity3.setEmail("test2@mail");
        memberEntity3.setOwner(false);
        memberEntity3.setLogin("dfaa");
        memberEntity3.setPassword("gsf");
        memberEntity3.setPhoneNumber("4234");
        return memberEntity3;
    }
}
