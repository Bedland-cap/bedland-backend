package com.capgemini.bedland.mappers;

import com.capgemini.bedland.dtos.PaymentDto;
import com.capgemini.bedland.entities.PaymentEntity;
import com.capgemini.bedland.enums.PaymentType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PaymentMapper {

    public PaymentDto entity2Dto(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }
        return PaymentDto.builder()
                .id(entity.getId())
                .version(entity.getVersion())
                .createDate(entity.getCreateDate())
                .updateDate(entity.getUpdateDate())
                .flatId(entity.getFlatEntity()
                              .getId())
                .expirationDate(entity.getExpirationDate())
                .paymentType(entity.getPaymentType()
                                   .toString())
                .paymentValue(entity.getPaymentValue())
                .build();
    }

    public List<PaymentDto> entities2DTOs(List<PaymentEntity> entities) {
        return entities.stream()
                .map(this::entity2Dto)
                .toList();
    }

    public PaymentEntity dto2Entity(PaymentDto dto) {
        if (dto == null) {
            return null;
        }
        PaymentEntity newPayment = new PaymentEntity();
        newPayment.setVersion(dto.getVersion());
        newPayment.setCreateDate(dto.getCreateDate());
        newPayment.setUpdateDate(dto.getUpdateDate());
        newPayment.setFlatEntity(null);
        newPayment.setExpirationDate(dto.getExpirationDate());
        newPayment.setPaymentType(PaymentType.valueOf(dto.getPaymentType()));
        newPayment.setPaymentValue(dto.getPaymentValue());
        if (dto.getId() != null) {
            newPayment.setId(dto.getId());
        }
        return newPayment;
    }

}
