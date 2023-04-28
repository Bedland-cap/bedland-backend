package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.NotificationSummaryDto;
import com.capgemini.bedland.entities.*;
import com.capgemini.bedland.enums.NotificationTarget;
import com.capgemini.bedland.enums.NotificationType;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.repositories.CustomIncidentRepository;
import com.capgemini.bedland.repositories.CustomPaymentRepository;
import com.capgemini.bedland.repositories.ManagerRepository;
import com.capgemini.bedland.repositories.OwnerRepository;
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

    @Autowired
    private OwnerRepository ownerRepository;

    @Override
    public List<NotificationSummaryDto> getGivenNumberOfLastNotificationsForGivenManager(Long managerId, Integer numberOfNotifications) {

        if (managerId == null || numberOfNotifications == null || numberOfNotifications < 1 || !managerRepository.existsById(managerId)) {
            throw new IllegalArgumentException("Incorrect arguments for getting notifications for manager");
        }
        List<PaymentEntity> payments = getCertainPaymentsForManager(managerId, numberOfNotifications);
        List<IncidentEntity> incidents = getCertainIncidentsForManager(managerId, numberOfNotifications);
        return buildNotificationDtoList(numberOfNotifications, payments, incidents, NotificationTarget.MANAGER);

    }

    @Override
    public List<NotificationSummaryDto> getGivenNumberOfLastNotificationsForGivenOwner(Long ownerId, Integer numberOfNotifications) {
        if (ownerId == null || numberOfNotifications == null || numberOfNotifications < 1 || !ownerRepository.existsById(ownerId)) {
            throw new IllegalArgumentException("Incorrect arguments for getting notifications for owner");
        }
        List<PaymentEntity> payments = getCertainPaymentsForOwner(ownerId, numberOfNotifications);
        List<IncidentEntity> incidents = getCertainIncidentsForOwner(ownerId, numberOfNotifications);
        return buildNotificationDtoList(numberOfNotifications, payments, incidents, NotificationTarget.OWNER);
    }

    private List<NotificationSummaryDto> convertPaymentsIntoNotifications(List<PaymentEntity> paymentEntities, NotificationTarget target) {
        List<NotificationSummaryDto> summaries = new LinkedList<>();

        if (!paymentEntities.isEmpty()) {
            paymentEntities.forEach(paymentEntity -> {
                NotificationSummaryDto notification = new NotificationSummaryDto();
                notification.setNotificationType(NotificationType.PAYMENT);
                if (target.equals(NotificationTarget.MANAGER)) {
                    notification.setDateOfLastChange(paymentEntity.getUpdateDate());
                    notification.setTitle(createPaymentNotificationTitleForManager(paymentEntity));
                }
                if (target.equals(NotificationTarget.OWNER)) {
                    notification.setDateOfLastChange(paymentEntity.getCreateDate());
                    notification.setTitle(createPaymentNotificationTitleForOwner(paymentEntity));
                }
                notification.setIsVisited(false);
                notification.setIdOfNotification(paymentEntity.getId());
                summaries.add(notification);
            });
        }
        return summaries;
    }

    private List<NotificationSummaryDto> convertIncidentsIntoNotifications(List<IncidentEntity> incidentEntities, NotificationTarget target) {
        List<NotificationSummaryDto> summaries = new LinkedList<>();

        if (!incidentEntities.isEmpty()) {
            incidentEntities.forEach(incidentEntity -> {
                NotificationSummaryDto notification = new NotificationSummaryDto();
                notification.setNotificationType(NotificationType.INCIDENT);

                if (target.equals(NotificationTarget.MANAGER)) {
                    notification.setTitle(createIncidentNotificationTitleForManager(incidentEntity));
                    notification.setDateOfLastChange(incidentEntity.getCreateDate());
                }
                if (target.equals(NotificationTarget.OWNER)) {
                    notification.setTitle(createIncidentNotificationTitleForOwner(incidentEntity));
                    notification.setDateOfLastChange(incidentEntity.getUpdateDate());
                }
                notification.setIsVisited(false);
                notification.setIdOfNotification(incidentEntity.getId());
                summaries.add(notification);
            });
        }
        return summaries;
    }

    private List<PaymentEntity> getCertainPaymentsForManager(Long managerId, int numberOfNotifications) {
        return customPaymentRepository.findLatestPaymentsForGivenManagerWithGivenLastStatus(managerId, numberOfNotifications, PaymentStatusName.PAID);
    }

    private List<IncidentEntity> getCertainIncidentsForManager(Long managerId, int numberOfNotifications) {
        return customIncidentRepository.findLatestCreatedIncidentsForGivenManager(managerId, numberOfNotifications);
    }

    private List<PaymentEntity> getCertainPaymentsForOwner(Long ownerId, int numberOfNotifications) {
        return customPaymentRepository.findLatestPaymentsForGivenOwnerWithGivenLastStatus(ownerId, numberOfNotifications, PaymentStatusName.UNPAID);
    }

    private List<IncidentEntity> getCertainIncidentsForOwner(Long ownerId, int numberOfNotifications) {
        return customIncidentRepository.findLatestUpdatedIncidentsForGivenOwner(ownerId, numberOfNotifications);
    }

    private List<NotificationSummaryDto> buildNotificationDtoList(int numberOfNotifications, List<PaymentEntity> paymentEntities, List<IncidentEntity> incidentEntities, NotificationTarget target) {

        List<NotificationSummaryDto> notifications = new LinkedList<>();
        notifications.addAll(convertPaymentsIntoNotifications(paymentEntities, target));
        notifications.addAll(convertIncidentsIntoNotifications(incidentEntities, target));
        notifications.sort(Comparator.comparing(NotificationSummaryDto::getDateOfLastChange).reversed());

        while (notifications.size() > numberOfNotifications) {
            notifications.remove(notifications.size() - 1);
        }
        return notifications;
    }

    private String createIncidentNotificationTitleForManager(IncidentEntity incident) {
        OwnerEntity owner = incident
                .getFlatEntity()
                .getFlatOwnerEntity();
        return String.format("New report from %s %s was sent.", owner.getName(), owner.getLastName());
    }

    private String createPaymentNotificationTitleForManager(PaymentEntity paymentEntity) {
        OwnerEntity owner = paymentEntity
                .getFlatEntity()
                .getFlatOwnerEntity();
        return String.format("New payment from %s %s was sent.", owner.getName(), owner.getLastName());
    }

    private String createPaymentNotificationTitleForOwner(PaymentEntity paymentEntity) {
        ManagerEntity manager = paymentEntity
                .getFlatEntity()
                .getBuildingEntity()
                .getManagerEntity();
        return String.format("New %s payment to %s %s.",paymentEntity.getLastPaymentStatusName().toString(), manager.getName(), manager.getLastName());
    }

    private String createIncidentNotificationTitleForOwner(IncidentEntity incident) {
        ManagerEntity manager = incident
                .getFlatEntity()
                .getBuildingEntity()
                .getManagerEntity();
        List<IncidentStatusEntity> statuses = incident.getIncidentStatusEntities();
        statuses.sort(Comparator.comparing(IncidentStatusEntity::getUpdateDate).reversed());
        return String.format("Report to %s %s changed status to: %s.", manager.getName(), manager.getLastName(), statuses.get(0).getIncidentStatusName().toString());
    }
}
