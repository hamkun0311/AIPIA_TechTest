package com.example.demo.controller.Order;

import com.example.demo.dto.Order.OrderCreateRequest;
import com.example.demo.dto.Order.OrderResponse;
import com.example.demo.model.Order.Order;
import com.example.demo.service.Order.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public OrderResponse create(@Valid @RequestBody OrderCreateRequest request) {
        Order order = orderService.createOrder(request.getMemberId(), request.getTotalAmount());
        return OrderResponse.from(order);
    }

    @GetMapping("/{id}")
    public OrderResponse get(@PathVariable Long id) {
        return OrderResponse.from(orderService.getOrder(id));
    }
}
