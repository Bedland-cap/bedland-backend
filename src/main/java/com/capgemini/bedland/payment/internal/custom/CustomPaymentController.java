package com.capgemini.bedland.payment.internal.custom;

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


}
