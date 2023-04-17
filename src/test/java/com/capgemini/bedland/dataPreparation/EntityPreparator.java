package com.capgemini.bedland.dataPreparation;

import com.capgemini.bedland.entities.*;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.enums.PaymentType;

import java.time.LocalDateTime;

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

    public static ManagerEntity prepareSecondTestManager() {
        ManagerEntity testManager1 = new ManagerEntity();
        testManager1.setName("managerNameTest2");
        testManager1.setLastName("managerLastNameTest2");
        testManager1.setPhoneNumber("22222");
        testManager1.setEmail("test222@mail");
        testManager1.setLogin("testLogin222");
        testManager1.setPassword("testPassword222");
        return testManager1;
    }

    public static OwnerEntity prepareFirstTestOwner() {
        String s = "owner1";
        OwnerEntity owner = new OwnerEntity();
        owner.setName(s);
        owner.setLastName(s);
        owner.setLogin(s);
        owner.setPhoneNumber("12432");
        owner.setPassword(s);
        owner.setEmail(s + "@email");
        return owner;
    }

    public static OwnerEntity prepareSecondTestOwner() {
        String s = "owner2";
        OwnerEntity owner = new OwnerEntity();
        owner.setName(s);
        owner.setLastName(s);
        owner.setLogin(s);
        owner.setPhoneNumber("12432");
        owner.setPassword(s);
        owner.setEmail(s + "@email");
        return owner;
    }

    public static OwnerEntity prepareThirdTestOwner() {
        String s = "owner3";
        OwnerEntity owner = new OwnerEntity();
        owner.setName(s);
        owner.setLastName(s);
        owner.setLogin(s);
        owner.setPhoneNumber("12432");
        owner.setPassword(s);
        owner.setEmail(s + "@email");
        return owner;
    }

    public static OwnerEntity prepareFourthTestOwner() {
        String s = "owner4";
        OwnerEntity owner = new OwnerEntity();
        owner.setName(s);
        owner.setLastName(s);
        owner.setLogin(s);
        owner.setPhoneNumber("12432");
        owner.setPassword(s);
        owner.setEmail(s + "@email");
        return owner;
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
        memberEntity1.setPhoneNumber("4314123");
        return memberEntity1;
    }

    public static MemberEntity prepareSecondTestMember_Owner(FlatEntity flatEntity) {
        MemberEntity memberEntity5 = new MemberEntity();
        memberEntity5.setFlatEntity(flatEntity);
        memberEntity5.setName("owner2");
        memberEntity5.setLastName("owner2");
        memberEntity5.setEmail("owner2@mail");
        memberEntity5.setPhoneNumber("4314123");
        return memberEntity5;
    }

    public static MemberEntity prepareFirstTestMember(FlatEntity flatEntity) {
        MemberEntity memberEntity2 = new MemberEntity();
        memberEntity2.setFlatEntity(flatEntity);
        memberEntity2.setName("testName11");
        memberEntity2.setLastName("testLastName11");
        memberEntity2.setEmail("test11@mail");
        memberEntity2.setPhoneNumber("43124123");
        return memberEntity2;
    }

    public static MemberEntity prepareSecondTestMember(FlatEntity flatEntity) {
        MemberEntity memberEntity3 = new MemberEntity();
        memberEntity3.setFlatEntity(flatEntity);
        memberEntity3.setName("testName2");
        memberEntity3.setLastName("testLastName2");
        memberEntity3.setEmail("test2@mail");
        memberEntity3.setPhoneNumber("4234");
        return memberEntity3;
    }

    public static PaymentEntity prepareFirstPayment(FlatEntity flatEntity) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setFlatEntity(flatEntity);
        paymentEntity.setPaymentType(PaymentType.INTERNET);
        paymentEntity.setPaymentValue(21.37);
        paymentEntity.setExpirationDate(LocalDateTime.now());
        paymentEntity.setLastPaymentStatusName(PaymentStatusName.UNPAID);
        return paymentEntity;
    }

    public static PaymentEntity prepareSecondPayment(FlatEntity flatEntity) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setFlatEntity(flatEntity);
        paymentEntity.setPaymentType(PaymentType.GAS);
        paymentEntity.setPaymentValue(200.99);
        paymentEntity.setExpirationDate(LocalDateTime.now());
        paymentEntity.setLastPaymentStatusName(PaymentStatusName.UNPAID);
        return paymentEntity;
    }

    public static PaymentEntity prepareThirdPayment(FlatEntity flatEntity) {
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setFlatEntity(flatEntity);
        paymentEntity.setPaymentType(PaymentType.WATER);
        paymentEntity.setPaymentValue(9999.99);
        paymentEntity.setExpirationDate(LocalDateTime.now());
        paymentEntity.setLastPaymentStatusName(PaymentStatusName.UNPAID);
        return paymentEntity;
    }

    public static PaymentStatusEntity prepareUNPAIDPaymentStatus(PaymentEntity paymentEntity) {
        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity();
        paymentStatusEntity.setPaymentEntity(paymentEntity);
        paymentStatusEntity.setPaymentStatusName(PaymentStatusName.UNPAID);
        return paymentStatusEntity;
    }

    public static PaymentStatusEntity preparePAIDPaymentStatus(PaymentEntity paymentEntity) {
        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity();
        paymentStatusEntity.setPaymentEntity(paymentEntity);
        paymentStatusEntity.setPaymentStatusName(PaymentStatusName.PAID);
        return paymentStatusEntity;
    }

    public static PaymentStatusEntity prepareEXPIREDPaymentStatus(PaymentEntity paymentEntity) {
        PaymentStatusEntity paymentStatusEntity = new PaymentStatusEntity();
        paymentStatusEntity.setPaymentEntity(paymentEntity);
        paymentStatusEntity.setPaymentStatusName(PaymentStatusName.EXPIRED);
        return paymentStatusEntity;
    }
}
