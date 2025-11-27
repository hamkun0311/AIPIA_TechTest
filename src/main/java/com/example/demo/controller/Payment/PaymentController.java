package com.example.demo.controller.Payment;

import com.example.demo.dto.Payment.PaymentCreateRequest;
import com.example.demo.dto.Payment.PaymentResponse;
import com.example.demo.model.Payment.Payment;
import com.example.demo.service.Payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public PaymentResponse pay(@Valid @RequestBody PaymentCreateRequest request) {
        Payment payment = paymentService.pay(
                request.getOrderId(),
                request.getMethod(),
                request.getAmount()
        );
        return PaymentResponse.from(payment);
    }

    @GetMapping("/{id}")
    public PaymentResponse get(@PathVariable Long id) {
        return PaymentResponse.from(paymentService.getPayment(id));
    }
}
