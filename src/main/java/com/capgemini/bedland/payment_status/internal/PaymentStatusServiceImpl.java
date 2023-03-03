package com.capgemini.bedland.payment_status.internal;

import com.capgemini.bedland.exceptions.NotFoundException;
import com.capgemini.bedland.payment.internal.PaymentRepository;
import com.capgemini.bedland.payment_status.api.PaymentStatusEntity;
import com.capgemini.bedland.payment_status.api.PaymentStatusName;
import com.capgemini.bedland.payment_status.api.PaymentStatusProvider;
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

    @Override
    public List<PaymentStatusDto> getAll() {
        return paymentStatusMapper.entities2DTOs(paymentStatusRepository.findAll());
    }

    @Override
    public PaymentStatusDto getById(Long id) {
        return paymentStatusMapper.entity2Dto(paymentStatusRepository.findById(id)
                                                                     .orElseThrow(() -> new NotFoundException(id)));
    }

    @Override
    public PaymentStatusDto create(PaymentStatusDto request) {
        if (request.getId() != null) {
            throw new IllegalArgumentException("Given request contains an ID. Payment can't be created");
        }
        PaymentStatusEntity createPaymentStatus = paymentStatusRepository.save(repackDtoToEntity(request));
        return paymentStatusMapper.entity2Dto(createPaymentStatus);
    }

    @Override
    public void createByPaymentId(Long paymentId) {
        PaymentStatusEntity createPaymentStatus = new PaymentStatusEntity();
        createPaymentStatus.setPaymentStatusName(PaymentStatusName.UNPAID);
        createPaymentStatus.setPaymentEntity(paymentRepository.findById(paymentId)
                                                              .orElseThrow(() -> new NotFoundException(paymentId)));
        paymentStatusRepository.save(createPaymentStatus);
    }

    @Override
    public void delete(Long id) {
        if (!paymentStatusRepository.existsById(id)) {
            throw new NotFoundException(id);
        }
        paymentStatusRepository.deleteById(id);
    }

    @Override
    public PaymentStatusDto update(PaymentStatusDto request) {
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
