package com.capgemini.bedland.controllers;


import com.capgemini.bedland.dtos.PaymentDto;
import com.capgemini.bedland.providers.PaymentProvider;
import com.capgemini.bedland.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentProvider paymentProvider;
    private final PaymentService paymentService;

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
