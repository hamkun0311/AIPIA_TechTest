package com.example.demo.service.Payment;

import com.example.demo.dao.Order.OrderRepository;
import com.example.demo.dao.Payment.PaymentRepository;
import com.example.demo.model.Order.Order;
import com.example.demo.model.Payment.Payment;
import com.example.demo.model.Payment.PaymentMethod;
import com.example.demo.model.Payment.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public Payment pay(Long orderId, PaymentMethod method, BigDecimal amount) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("주문이 존재하지 않습니다."));

        // 중복 결제 체크
        paymentRepository.findByOrderId(orderId)
                .ifPresent(p -> { throw new IllegalStateException("이미 결제가 존재합니다."); });

        if (order.getTotalAmount().compareTo(amount) != 0) {
            throw new IllegalArgumentException("결제 금액이 주문 금액과 다릅니다.");
        }

        Payment payment = new Payment(order, method, amount);

        // 바로 결제 완료 처리
        payment.markCompleted();

        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("결제가 존재하지 않습니다."));
    }
}
