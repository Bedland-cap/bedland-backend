package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.PaymentDto;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.PaymentMapper;
import com.capgemini.bedland.providers.PaymentProvider;
import com.capgemini.bedland.repositories.FlatRepository;
import com.capgemini.bedland.repositories.PaymentRepository;
import com.capgemini.bedland.services.PaymentService;
import com.capgemini.bedland.services.PaymentStatusService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Transactional
@Service
public class PaymentServiceImpl implements PaymentService, PaymentProvider {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private PaymentStatusService paymentStatusService;
    private final String idIsNull = "Given ID is null";

    @Override
    public List<PaymentDto> getAll() {
        return paymentMapper.entities2DTOs(paymentRepository.findAll());
    }

    @Override
    public PaymentDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        return paymentMapper.entity2Dto(paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public PaymentDto create(PaymentDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Payment can't be created");
        }
        if (request.getPaymentValue() < 0) {
            throw new IllegalArgumentException("Payment value must be grater than 0");
        }
        PaymentEntity paymentEntity = repackDtoToEntity(request);
        paymentEntity.setLastPaymentStatusName(PaymentStatusName.UNPAID);
        PaymentEntity createPayment = paymentRepository.save(paymentEntity);

        paymentStatusService.createByPaymentId(createPayment.getId());
        return paymentMapper.entity2Dto(createPayment);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        if (!paymentRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentDto update(PaymentDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!paymentRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        PaymentEntity paymentEntity = repackDtoToEntity(request);
        paymentEntity.setLastPaymentStatusName(paymentRepository.findById(request.getId()).orElseThrow(() -> new NotFoundException(request.getId())).getLastPaymentStatusName());
        PaymentEntity updatePayment = paymentRepository.save(paymentEntity);

        return paymentMapper.entity2Dto(updatePayment);
    }

    private PaymentEntity repackDtoToEntity(PaymentDto dto) {
        PaymentEntity entity = paymentMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        return entity;
    }

}
