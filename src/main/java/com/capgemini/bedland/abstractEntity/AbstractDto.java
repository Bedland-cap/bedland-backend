package com.capgemini.bedland.abstractEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class AbstractDto {

    private Long id;
    private Long version;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

}
