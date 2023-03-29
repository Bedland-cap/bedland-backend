package com.capgemini.bedland.services.impl;

import com.capgemini.bedland.dtos.PaymentStatusDto;
import com.capgemini.bedland.entities.PaymentStatusEntity;
import com.capgemini.bedland.enums.PaymentStatusName;
import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.mappers.PaymentStatusMapper;
import com.capgemini.bedland.providers.PaymentStatusProvider;
import com.capgemini.bedland.repositories.PaymentRepository;
import com.capgemini.bedland.repositories.PaymentStatusRepository;
import com.capgemini.bedland.services.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentStatusServiceImpl implements PaymentStatusService, PaymentStatusProvider {

    @Autowired
    PaymentStatusMapper paymentStatusMapper;
    @Autowired
    PaymentStatusRepository paymentStatusRepository;
    @Autowired
    PaymentRepository paymentRepository;

    private final String idIsNull = "Given ID is null";

    @Override
    public List<PaymentStatusDto> getAll() {
        return paymentStatusMapper.entities2DTOs(paymentStatusRepository.findAll());
    }

    @Override
    public PaymentStatusDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        return paymentStatusMapper.entity2Dto(paymentStatusRepository.findById(id)
                                                                     .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public PaymentStatusDto create(PaymentStatusDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Payment can't be created");
        }
        PaymentStatusEntity createPaymentStatus = paymentStatusRepository.save(repackDtoToEntity(request));
        return paymentStatusMapper.entity2Dto(createPaymentStatus);
    }

    @Override
    public void createByPaymentId(Long paymentId) {
        if (paymentId == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        PaymentStatusEntity createPaymentStatus = new PaymentStatusEntity();
        createPaymentStatus.setPaymentStatusName(PaymentStatusName.UNPAID);
        createPaymentStatus.setPaymentEntity(paymentRepository.findById(paymentId)
                                                              .orElseThrow(() -> new NotFoundException(paymentId)));
        paymentStatusRepository.save(createPaymentStatus);
    }

    @Override
    public void delete(Long id) {
        if (id == null) {
            throw new IllegalArgumentException(idIsNull);
        }
        if (!paymentStatusRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        paymentStatusRepository.deleteById(id);
    }

    @Override
    public PaymentStatusDto update(PaymentStatusDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Given request is null");
        }
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!paymentStatusRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        PaymentStatusEntity updatePaymentStatus = paymentStatusRepository.save(repackDtoToEntity(request));
        return paymentStatusMapper.entity2Dto(updatePaymentStatus);
    }

    private PaymentStatusEntity repackDtoToEntity(PaymentStatusDto dto) {
        PaymentStatusEntity entity = paymentStatusMapper.dto2Entity(dto);
        entity.setPaymentEntity(paymentRepository.findById(dto.getPaymentId())
                                                 .orElseThrow(() -> new NotFoundException(dto.getPaymentId())));
        return entity;
    }

}
