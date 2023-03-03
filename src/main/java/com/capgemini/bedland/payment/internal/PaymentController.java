package com.capgemini.bedland.payment.internal;

import com.capgemini.bedland.payment.api.PaymentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping("/payment")
class PaymentController {

    @Autowired
    private PaymentProvider paymentProvider;
    @Autowired
    private PaymentService paymentService;

    @GetMapping()
    List<PaymentDto> getAll() {
        return paymentProvider.getAll();
    }

    @GetMapping("/{id}")
    PaymentDto getById(@PathVariable Long id) {
        return paymentProvider.getById(id);
    }

    @PostMapping
    @ResponseStatus(CREATED)
    PaymentDto create(@RequestBody PaymentDto request) {
        return paymentService.create(request);
    }

    @PatchMapping()
    PaymentDto update(@RequestBody PaymentDto request) {
        return paymentService.update(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id) {
        paymentService.delete(id);
    }

}
