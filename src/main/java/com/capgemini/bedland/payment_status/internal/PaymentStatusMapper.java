package com.capgemini.bedland.payment_status.internal;

import com.capgemini.bedland.payment_status.api.PaymentStatusEntity;
import com.capgemini.bedland.payment_status.api.PaymentStatusName;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class PaymentStatusMapper {

    public PaymentStatusDto entity2Dto(PaymentStatusEntity entity) {
        if (entity == null) {
            return null;
        }
        return PaymentStatusDto.builder()
                               .id(entity.getId())
                               .version(entity.getVersion())
                               .createDate(entity.getCreateDate())
                               .updateDate(entity.getUpdateDate())
                               .paymentId(entity.getPaymentEntity()
                                                .getId())
                               .paymentStatusName(entity.getPaymentStatusName()
                                                        .toString())
                               .build();
    }

    public List<PaymentStatusDto> entities2DTOs(List<PaymentStatusEntity> entities) {
        return entities.stream()
                       .map(this::entity2Dto)
                       .toList();
    }

    public PaymentStatusEntity dto2Entity(PaymentStatusDto dto) {
        if (dto == null) {
            return null;
        }
        PaymentStatusEntity newPaymentStatus = new PaymentStatusEntity();
        newPaymentStatus.setVersion(dto.getVersion());
        newPaymentStatus.setCreateDate(dto.getCreateDate());
        newPaymentStatus.setUpdateDate(dto.getUpdateDate());
        newPaymentStatus.setPaymentEntity(null);
        if (dto.getPaymentStatusName() == null) {
            newPaymentStatus.setPaymentStatusName(PaymentStatusName.UNPAID);
        } else {
            newPaymentStatus.setPaymentStatusName(PaymentStatusName.valueOf(dto.getPaymentStatusName()));
        }
        if (dto.getId() != null) {
            newPaymentStatus.setId(dto.getId());
        }
        return newPaymentStatus;
    }

}
