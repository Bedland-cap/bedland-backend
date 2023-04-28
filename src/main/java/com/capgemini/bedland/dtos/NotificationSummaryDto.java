package com.capgemini.bedland.dtos;

import com.capgemini.bedland.enums.NotificationType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@SuperBuilder
public class NotificationSummaryDto {

private Long idOfNotification;
private LocalDateTime dateOfLastChange;
private NotificationType notificationType;
private String title;
private Boolean isVisited;
}
