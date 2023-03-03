package com.capgemini.bedland.payment_status.internal;

import com.capgemini.bedland.payment_status.api.PaymentStatusProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/payment-status")
class PaymentStatusController {

    @Autowired
    private PaymentStatusService paymentStatusService;
    @Autowired
    private PaymentStatusProvider paymentStatusProvider;

    @GetMapping()
    List<PaymentStatusDto> getAll() {
        return paymentStatusProvider.getAll();
    }

    @GetMapping("/{id}")
    PaymentStatusDto getById(@PathVariable Long id) {
        return paymentStatusProvider.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    PaymentStatusDto create(@RequestBody PaymentStatusDto request) {
        return paymentStatusService.create(request);
    }

    @PatchMapping()
    PaymentStatusDto update(@RequestBody PaymentStatusDto request) {
        return paymentStatusService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        paymentStatusService.delete(id);
    }

}
