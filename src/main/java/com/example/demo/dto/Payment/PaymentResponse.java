package com.example.demo.dto.Payment;

import com.example.demo.model.Payment.Payment;
import com.example.demo.model.Payment.PaymentMethod;
import com.example.demo.model.Payment.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentResponse {

    private Long id;
    private Long orderId;
    private PaymentStatus status;
    private PaymentMethod method;
    private BigDecimal amount;
    private LocalDateTime paidAt;

    public PaymentResponse(Long id, Long orderId, PaymentStatus status, PaymentMethod method, BigDecimal amount, LocalDateTime paidAt) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.method = method;
        this.amount = amount;
        this.paidAt = paidAt;
    }

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus(),
                payment.getMethod(),
                payment.getAmount(),
                payment.getPaidAt()
        );
    }

    public Long getId() { return id; }
    public Long getOrderId() { return orderId; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
