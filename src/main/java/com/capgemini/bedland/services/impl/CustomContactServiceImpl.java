package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.dtos.ContactSummaryDto;
import com.capgemini.bedland.entities.FlatEntity;
import com.capgemini.bedland.mappers.AnnouncementMapper;
import com.capgemini.bedland.repositories.AnnouncementRepository;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
import com.capgemini.bedland.services.CustomContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class CustomContactServiceImpl implements CustomContactService {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private FlatRepository flatRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    @Autowired
    private AnnouncementRepository announcementRepository;

    @Autowired
    private AnnouncementMapper announcementMapper;

    @Override
    public List<ContactSummaryDto> getContactsForGivenManager(Long managerId) {

        if (managerId == null || !managerRepository.existsById(managerId)) {
            throw new IllegalArgumentException("Incorrect manager for getting contacts");
        }
        return createContactSummariesFromGivenFlats(getFlatsWhereAnnouncementsToManagerWereMade(managerId));
    }

    @Override
    public List<AnnouncementDto> getMessagesBetweenOwnerAndManagerForGivenOwner(Long ownerId) {
        if (ownerId == null || !ownerRepository.existsById(ownerId)) {
            throw new IllegalArgumentException("Incorrect owner for getting messages");
        }
        return announcementMapper.entities2DTOs(
                announcementRepository
                        .getAnnouncementsForGivenOwner(ownerId).stream().filter(a -> a.getBuildingEntity() == null && a.getFlatEntity() != null)
                        .toList());
    }

    private List<FlatEntity> getFlatsWhereAnnouncementsToManagerWereMade(Long managerId) {
        return flatRepository.findFlatsWhichOwnersMadeAnnouncementToManager(managerId);
    }

    private List<ContactSummaryDto> createContactSummariesFromGivenFlats(List<FlatEntity> flatEntities) {
        List<ContactSummaryDto> contactSummaries = new LinkedList<>();

        flatEntities.forEach(f -> contactSummaries
                .add(ContactSummaryDto
                        .builder()
                        .residentId(f.getFlatOwnerEntity().getId())
                        .residentName(f.getFlatOwnerEntity().getName())
                        .residentLastName(f.getFlatOwnerEntity().getLastName())
                        .flatId(f.getId())
                        .build()));

        return contactSummaries;
    }

}
