package com.capgemini.bedland.dtos;

import com.capgemini.bedland.enums.MonthlyFlatPaymentStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@SuperBuilder
public class FlatDetailsDto {

    private String flatNumber;
    private int floor;
    private String owner;
    private List<String> residents;
    private MonthlyFlatPaymentStatus monthlyPayments;
    private LocalDateTime lastMaintenance;
}
