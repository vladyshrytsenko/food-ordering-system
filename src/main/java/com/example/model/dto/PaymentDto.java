package com.example.model.dto;

import com.example.model.entity.Payment;
import com.example.model.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentDto {

    private String id;
    private Long orderId;
    private Float totalPrice;
    private String status;

    public static PaymentDto toDto(Payment entity) {

        return PaymentDto.builder()
            .id(entity.getId())
            .orderId(entity.getOrder().getId())
            .totalPrice(entity.getTotalPrice())
            .status(entity.getStatus().name())
            .build();
    }

    public static Payment toEntity(PaymentDto dto) {

        return Payment.builder()
            .id(dto.getId())
            .totalPrice(dto.getTotalPrice())
            .status(PaymentStatus.of(dto.getStatus()))
            .build();
    }
}