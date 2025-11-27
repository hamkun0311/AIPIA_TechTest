package com.example.demo.dto.Payment;

import com.example.demo.model.Payment.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class PaymentCreateRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMethod method;

    @NotNull
    private BigDecimal amount;

    public PaymentCreateRequest(Long orderId, PaymentMethod method, BigDecimal amount) {
        this.orderId = orderId;
        this.method = method;
        this.amount = amount;
    }

    public Long getOrderId() { return orderId; }
    public PaymentMethod getMethod() { return method; }
    public BigDecimal getAmount() { return amount; }
}

