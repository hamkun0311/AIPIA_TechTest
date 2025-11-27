package com.example.demo.model.Order;

import com.example.demo.model.Member.Member;
import com.example.demo.model.Payment.Payment;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders") // order는 예약어일 수 있어서 테이블명 지정
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ★ Member : Order = 1 : N
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    // ★ Order : Payment = 1 : 1 (양방향)
    @OneToOne(mappedBy = "order", fetch = FetchType.LAZY)
    private Payment payment;

    protected Order() {
    }

    public Order(Member member, BigDecimal totalAmount) {
        this.member = member;
        this.totalAmount = totalAmount;
        this.orderDate = LocalDateTime.now();
        this.status = OrderStatus.CREATED;
    }

    // 비즈니스 메서드
    public void markPaid() {
        this.status = OrderStatus.PAID;
    }

    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }

    // getter들
    public Long getId() { return id; }
    public Member getMember() { return member; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public Payment getPayment() { return payment; }

    // 연관관계 편의 메서드 (필요하면)
    public void setMember(Member member) {
        this.member = member;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }
}
