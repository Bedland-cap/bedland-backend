package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.OwnerSummaryDto;
import com.capgemini.bedland.entities.BuildingEntity;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.repositories.BuildingRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.services.CustomOwnerService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class CustomOwnerServiceImpl implements CustomOwnerService {

    @Autowired
    OwnerRepository ownerRepository;
    @Autowired
    BuildingRepository buildingRepository;
    @Autowired
    FlatRepository flatRepository;
    @Autowired
    ManagerRepository managerRepository;

    @Override
    public List<OwnerSummaryDto> findAllOwnersForGivenManager(Long managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager ID can't be null");
        }
        if (!managerRepository.existsById(managerId)) {
            throw new NotFoundException("Manager ID doesn't exist in DB");
        }
        return buildOwnerSummaryDtosForGivenManager(managerId);
    }

    private List<OwnerSummaryDto> buildOwnerSummaryDtosForGivenManager(Long managerId) {
        List<OwnerEntity> ownerForManager = ownerRepository.findAllOwnerForGivenManager(managerId);
        List<OwnerSummaryDto> owners = new LinkedList<>();
        if (!ownerForManager.isEmpty()) {
            ownerForManager.forEach(owner -> {
                List<FlatEntity> flatsForOwner = flatRepository.findFlatsForOwner(owner.getId());
                if (!flatsForOwner.isEmpty()) {
                    flatsForOwner.forEach(flat -> {
                        Optional<BuildingEntity> buildingForFlat = buildingRepository.findById(flat.getBuildingEntity().getId());
                        owners.add(OwnerSummaryDto.builder()
                                                  .buildingName(buildingForFlat.get().getBuildingName())
                                                  .flatNumber(flat.getNumber())
                                                  .flatFloor(flat.getFloor())
                                                  .flatId(flat.getId())
                                                  .ownerNameAndLastName(owner.getName() + " " + owner.getLastName())
                                                  .ownerPhone(owner.getPhoneNumber())
                                                  .ownerId(owner.getId())
                                                  .build());
                    });
                }
            });
        }
        return owners;
    }

}
