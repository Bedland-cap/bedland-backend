package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.NotificationSummaryDto;
import com.capgemini.bedland.services.CustomNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CustomNotificationController {

    private final CustomNotificationService customNotificationService;

    @GetMapping(path = "/notifications_summary", params = {"manager_id", "number_of_last_notifications"})
    List<NotificationSummaryDto> getLastNotificationsForGivenManager(@RequestParam Long manager_id, @RequestParam Integer number_of_last_notifications) {
        return customNotificationService.getGivenNumberOfLastNotificationsForGivenManager(manager_id, number_of_last_notifications);
    }

}
