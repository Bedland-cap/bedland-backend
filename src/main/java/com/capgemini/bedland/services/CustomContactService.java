package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.dtos.ContactSummaryDto;

import java.util.List;

public interface CustomContactService {

   List<ContactSummaryDto> getContactsForGivenManager(Long managerId);
   List<AnnouncementDto> getMessagesBetweenOwnerAndManagerForGivenOwner(Long ownerId);
}
