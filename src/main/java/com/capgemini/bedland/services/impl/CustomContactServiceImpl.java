package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.ContactSummaryDto;
import com.capgemini.bedland.entities.AnnouncementEntity;
import com.capgemini.bedland.entities.MemberEntity;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.MemberRepository;
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
    private MemberRepository memberRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<ContactSummaryDto> getContactsForGivenManager(Long managerId) {

        if (managerId == null || !managerRepository.existsById(managerId)) {
            throw new IllegalArgumentException("Incorrect manager for getting contacts");
        }
        return createContactSummariesFromGivenMembers(getMembersWhoMadeAnnouncementsToManager(managerId));
    }

    private List<MemberEntity> getMembersWhoMadeAnnouncementsToManager(Long managerId) {
        return memberRepository.findMembersByManager(managerId)
                .stream()
                .filter(m -> !m.getFlatEntity().getAnnouncementEntities().isEmpty() && m.isOwner() && !m.getFlatEntity()
                        .getAnnouncementEntities()
                        .stream()
                        .filter(AnnouncementEntity::isToManager)
                        .toList()
                        .isEmpty())
                .toList();
    }

    private List<ContactSummaryDto> createContactSummariesFromGivenMembers(List<MemberEntity> members) {
        List<ContactSummaryDto> contactSummaries = new LinkedList<>();

        members.forEach(m -> contactSummaries
                .add(ContactSummaryDto
                        .builder()
                        .residentId(m.getId())
                        .residentName(m.getName())
                        .residentLastName(m.getLastName())
                        .flatId(m.getFlatEntity().getId())
                        .build()));

        return contactSummaries;
    }

}
