package com.capgemini.bedland.services;

import com.capgemini.bedland.dtos.NotificationSummaryDto;

import java.util.List;

public interface CustomNotificationService {

    List<NotificationSummaryDto> getGivenNumberOfLastNotificationsForGivenManager(Long managerId, Integer numberOfNotifications);
    List<NotificationSummaryDto> getGivenNumberOfLastNotificationsForGivenOwner(Long ownerId, Integer numberOfNotifications);
}
