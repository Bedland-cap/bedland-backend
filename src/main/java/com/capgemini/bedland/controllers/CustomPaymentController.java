package com.capgemini.bedland.controllers;

import com.capgemini.bedland.dtos.PaymentSummaryDto;
import com.capgemini.bedland.services.CustomPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CustomPaymentController {

    private final CustomPaymentService customPaymentService;

    @GetMapping(path = "/payments_summary", params = {"manager_id"})
    List<PaymentSummaryDto> findPaymentsForGivenManager(@RequestParam Long manager_id) {
        return customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenManager(manager_id);
    }

    @GetMapping(path = "/payments_summary_resident", params = {"resident_id"})//resident = owner
    List<PaymentSummaryDto> findPaymentsForGivenOwner(@RequestParam Long resident_id) {
        return customPaymentService.findAllPaymentsWithStatusesAndTheirAmountsForGivenOwnerInActualMonth(resident_id);
    }
}
