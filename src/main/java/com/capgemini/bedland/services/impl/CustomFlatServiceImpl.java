package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.FlatDetailsDto;
import com.capgemini.bedland.dtos.FlatShortenDetailsDto;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.entities.MemberEntity;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.MonthlyFlatPaymentStatus;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.services.CustomFlatService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Service
@Transactional
public class CustomFlatServiceImpl implements CustomFlatService {

    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public List<FlatDetailsDto> getFlatDetailsForGivenManagerInGivenBuilding(Long managerId, Long buildingId) {
        if (managerId == null || buildingId == null) {
            throw new IllegalArgumentException("Manager and building can't be null");
        }
        if (!managerRepository.existsById(managerId) || !buildingRepository.existsById(buildingId)) {
            throw new IllegalArgumentException("Manager or building not found in DB");
        }
        if (!managerId.equals(buildingRepository.findById(buildingId).orElseThrow(() -> new NotFoundException(buildingId)).getManagerEntity().getId())) {
            throw new IllegalArgumentException("Building is not assigned to given manager");
        }
        return collectFlatDetailsForGivenManagerAndBuilding(managerId, buildingId);
    }

    @Override
    public List<FlatShortenDetailsDto> getFlatsForGivenOwner(Long ownerId) {
        if (ownerId == null || !ownerRepository.existsById(ownerId)) {
            throw new IllegalArgumentException("Incorrect owner to get flats for");
        }
        return collectFlatsForOwner(ownerId);
    }


    private String getFlatOwner(FlatEntity flat) {
        return flat.getFlatOwnerEntity().getName() + " " + flat.getFlatOwnerEntity().getLastName();
    }

    private List<String> getFlatResidents(FlatEntity flat) {
        List<MemberEntity> residents = flat.getFlatMembers();
        List<String> residentsStrings = new LinkedList<>();
        residents.forEach(r -> {
            String s = r.getName() + " " + r.getLastName();
            residentsStrings.add(s);
        });
        return residentsStrings;
    }

    private MonthlyFlatPaymentStatus getOverallMonthlyPayments(FlatEntity flat) {
        MonthlyFlatPaymentStatus monthlyFlatPaymentStatus = MonthlyFlatPaymentStatus.INCOMPLETE;

        List<PaymentEntity> unpaidPayments = flat.getPaymentEntities().stream().filter(paymentEntity -> paymentEntity.getLastPaymentStatusName().equals(PaymentStatusName.UNPAID)).toList();
        List<PaymentEntity> expiredPayments = flat.getPaymentEntities().stream().filter(paymentEntity -> paymentEntity.getLastPaymentStatusName().equals(PaymentStatusName.EXPIRED)).toList();

        if (expiredPayments.isEmpty() && unpaidPayments.isEmpty()) {
            monthlyFlatPaymentStatus = MonthlyFlatPaymentStatus.SUBMITTED;
        }
        if (!expiredPayments.isEmpty()) {
            monthlyFlatPaymentStatus = MonthlyFlatPaymentStatus.ARREARAGE;
        }
        return monthlyFlatPaymentStatus;
    }

    private LocalDateTime getLastPaymentDate(FlatEntity flat) {

        List<PaymentEntity> paymentEntities = flat.getPaymentEntities();
        paymentEntities.sort(Comparator.comparing(PaymentEntity::getUpdateDate).reversed());
        return paymentEntities.get(0).getUpdateDate();
    }

    private List<FlatDetailsDto> collectFlatDetailsForGivenManagerAndBuilding(Long managerId, Long buildingId) {

        List<FlatDetailsDto> flatDetails = new LinkedList<>();
        List<FlatEntity> flats = flatRepository.findFlatsForGivenManagerInGivenBuilding(managerId, buildingId);
        flats.forEach(flat -> flatDetails.add(FlatDetailsDto.builder().flatNumber(flat.getNumber()).floor(flat.getFloor()).owner(getFlatOwner(flat)).residents(getFlatResidents(flat)).lastMaintenance(getLastPaymentDate(flat)).monthlyPayments(getOverallMonthlyPayments(flat)).build()));
        return flatDetails;
    }

    private List<FlatShortenDetailsDto> collectFlatsForOwner(Long ownerId) {

        List<FlatEntity> flatsForOwner = flatRepository.findFlatsForOwner(ownerId);
        List<FlatShortenDetailsDto> flats = new LinkedList<>();
        if (!flatsForOwner.isEmpty()) {
            flatsForOwner.forEach(flat -> flats
                    .add(FlatShortenDetailsDto
                            .builder()
                            .flatAddress(flat.getBuildingEntity().getAddress())
                            .flatId(flat.getId())
                            .flatNumber(flat.getNumber())
                            .build()));
        }
        return flats;
    }
}
