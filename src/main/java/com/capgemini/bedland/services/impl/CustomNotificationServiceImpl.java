package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.NotificationSummaryDto;
import com.capgemini.bedland.entities.IncidentEntity;
import com.capgemini.bedland.entities.OwnerEntity;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.NotificationType;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.repositories.CustomIncidentRepository;
import com.capgemini.bedland.repositories.CustomPaymentRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.services.CustomNotificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Transactional
@Service
public class CustomNotificationServiceImpl implements CustomNotificationService {

    @Autowired
    private CustomPaymentRepository customPaymentRepository;

    @Autowired
    private CustomIncidentRepository customIncidentRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public List<NotificationSummaryDto> getGivenNumberOfLastNotificationsForGivenManager(Long managerId, Integer numberOfNotifications) {

        if (managerId == null || numberOfNotifications == null || !managerRepository.existsById(managerId)) {
            throw new IllegalArgumentException("Incorrect arguments for getting notifications for manager");
        }

        List<NotificationSummaryDto> resultSummaries = new LinkedList<>();
        prepareNotificationDtoList(managerId, numberOfNotifications, resultSummaries);
        return resultSummaries;
    }

    private List<NotificationSummaryDto> convertPaymentsIntoNotifications(List<PaymentEntity> paymentEntities) {
        List<NotificationSummaryDto> summaries = new LinkedList<>();

        if (!paymentEntities.isEmpty()) {
            paymentEntities.forEach(paymentEntity -> {
                NotificationSummaryDto notification = new NotificationSummaryDto();
                notification.setNotificationType(NotificationType.PAYMENT);
                notification.setDateOfLastChange(paymentEntity.getUpdateDate());
                notification.setTitle(createPaymentNotificationTitle(paymentEntity));
                notification.setIsVisited(false);
                notification.setIdOfNotification(paymentEntity.getId());
                summaries.add(notification);
            });
        }
        return summaries;
    }

    private List<NotificationSummaryDto> convertIncidentsIntoNotifications(List<IncidentEntity> incidentEntities) {
        List<NotificationSummaryDto> summaries = new LinkedList<>();

        if (!incidentEntities.isEmpty()) {
            incidentEntities.forEach(incidentEntity -> {
                NotificationSummaryDto notification = new NotificationSummaryDto();
                notification.setNotificationType(NotificationType.INCIDENT);
                notification.setDateOfLastChange(incidentEntity.getCreateDate());
                notification.setTitle(createIncidentNotificationTitle(incidentEntity));
                notification.setIsVisited(false);
                notification.setIdOfNotification(incidentEntity.getId());
                summaries.add(notification);
            });
        }
        return summaries;
    }

    private void prepareNotificationDtoList(Long managerId, int numberOfNotifications, List<NotificationSummaryDto> notifications) {

        List<PaymentEntity> paymentEntities = customPaymentRepository.findLatestPaymentsForGivenManagerWithGivenLastStatus(managerId, numberOfNotifications, PaymentStatusName.PAID);
        List<IncidentEntity> incidentEntities = customIncidentRepository.findLatestCreatedIncidentsForGivenManager(managerId, numberOfNotifications);
        notifications.addAll(convertPaymentsIntoNotifications(paymentEntities));
        notifications.addAll(convertIncidentsIntoNotifications(incidentEntities));
        notifications.sort(Comparator.comparing(NotificationSummaryDto::getDateOfLastChange).reversed());

        while (notifications.size() > numberOfNotifications) {
            notifications.remove(notifications.size() - 1);
        }
    }

    private String createIncidentNotificationTitle(IncidentEntity incident) {
        OwnerEntity owner = incident
                .getFlatEntity()
                .getFlatOwnerEntity();
        return String.format("New report from %s %s was sent.", owner.getName(), owner.getLastName());
    }

    private String createPaymentNotificationTitle(PaymentEntity paymentEntity) {
        OwnerEntity owner = paymentEntity
                .getFlatEntity()
                .getFlatOwnerEntity();
        return String.format("New payment from %s %s was sent.", owner.getName(), owner.getLastName());
    }

}
