package com.example.demo.model.Payment;

import com.example.demo.model.Order.Order;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ★ Payment : Order = 1 : 1
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private LocalDateTime paidAt;

    protected Payment() {
    }

    public Payment(Order order, PaymentMethod method, BigDecimal amount) {
        this.order = order;
        this.method = method;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
    }

    // 비즈니스 메서드
    public void markCompleted() {
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
        // 주문 상태도 함께 변경
        this.order.markPaid();
    }

    public void markFailed() {
        this.status = PaymentStatus.FAILED;
    }

    // getter들
    public Long getId() { return id; }
    public Order getOrder() { return order; }
    public PaymentStatus getStatus() { return status; }
    public PaymentMethod getMethod() { return method; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
