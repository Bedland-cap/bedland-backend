package com.capgemini.bedland.payment.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.flat.internal.FlatRepository;
import com.capgemini.bedland.payment.api.PaymentEntity;
import com.capgemini.bedland.payment.api.PaymentProvider;
import com.capgemini.bedland.payment_status.internal.PaymentStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PaymentServiceImpl implements PaymentService, PaymentProvider {

    @Autowired
    private PaymentMapper paymentMapper;
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FlatRepository flatRepository;
    @Autowired
    private PaymentStatusService paymentStatusService;

    @Override
    public List<PaymentDto> getAll() {
        return paymentMapper.entities2DTOs(paymentRepository.findAll());
    }

    @Override
    public PaymentDto getById(Long id) {
        return paymentMapper.entity2Dto(paymentRepository.findById(id)
                                                         .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public PaymentDto create(PaymentDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Payment can't be created");
        }
        PaymentEntity createPayment = paymentRepository.save(repackDtoToEntity(request));
        paymentStatusService.createByPaymentId(createPayment.getId());
        return paymentMapper.entity2Dto(createPayment);
    }

    @Override
    public void delete(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        paymentRepository.deleteById(id);
    }

    @Override
    public PaymentDto update(PaymentDto request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("Given request has no ID");
        }
        if (!paymentRepository.existsById(request.getId())) {
            throw new NotFoundException(request.getId());
        }
        PaymentEntity updatePayment = paymentRepository.save(repackDtoToEntity(request));
        return paymentMapper.entity2Dto(updatePayment);
    }

    private PaymentEntity repackDtoToEntity(PaymentDto dto) {
        PaymentEntity entity = paymentMapper.dto2Entity(dto);
        entity.setFlatEntity(flatRepository.findById(dto.getFlatId())
                                           .orElseThrow(() -> new NotFoundException(dto.getFlatId())));
        return entity;
    }

}
