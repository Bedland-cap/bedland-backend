package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.AnnouncementDto;
import com.capgemini.bedland.dtos.ContactSummaryDto;
import com.capgemini.bedland.services.CustomContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CustomContactController {

    @Autowired
    private final CustomContactService customContactService;

    @GetMapping(path = "/contacts_summary", params = {"manager_id"})
    List<ContactSummaryDto> getContactsSummariesForGivenManager(@RequestParam Long manager_id) {
        return customContactService.getContactsForGivenManager(manager_id);
    }

    @GetMapping(path = "/messages_resident", params = {"resident_id"})
    List<AnnouncementDto> getMessageSummariesForGivenOwner(@RequestParam Long resident_id) {
        return customContactService.getMessagesBetweenOwnerAndManagerForGivenOwner(resident_id);
    }
}
