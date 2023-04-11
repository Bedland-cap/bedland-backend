package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.ContactSummaryDto;

import java.util.List;

public interface CustomContactService {

   List<ContactSummaryDto> getContactsForGivenManager(Long managerId);

}
