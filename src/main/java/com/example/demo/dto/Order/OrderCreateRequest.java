package com.example.demo.dto.Order;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderCreateRequest {

    @NotNull
    private Long memberId;

    @NotNull
    private BigDecimal totalAmount;

    public OrderCreateRequest(Long memberId, BigDecimal totalAmount) {
        this.memberId = memberId;
        this.totalAmount = totalAmount;
    }

    public Long getMemberId() { return memberId; }
    public BigDecimal getTotalAmount() { return totalAmount; }
}
