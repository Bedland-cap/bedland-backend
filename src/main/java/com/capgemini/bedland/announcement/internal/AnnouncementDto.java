package com.capgemini.bedland.announcement.internal;

import com.capgemini.bedland.abstractEntity.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AnnouncementDto extends AbstractDto {

    private Long flatId;
    private Long buildingId;
    private String title;
    private String description;

}
