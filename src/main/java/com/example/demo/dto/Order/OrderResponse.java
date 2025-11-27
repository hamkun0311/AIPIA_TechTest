package com.example.demo.dto.Order;

import com.example.demo.model.Order.Order;
import com.example.demo.model.Order.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderResponse {

    private Long id;
    private Long memberId;
    private OrderStatus status;
    private LocalDateTime orderDate;
    private BigDecimal totalAmount;

    public OrderResponse(Long id, Long memberId, OrderStatus status, LocalDateTime orderDate, BigDecimal totalAmount) {
        this.id = id;
        this.memberId = memberId;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
    }

    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getMember().getId(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }

    // getter
    public Long getId() { return id; }
    public Long getMemberId() { return memberId; }
    public OrderStatus getStatus() { return status; }
    public LocalDateTime getOrderDate() { return orderDate; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
