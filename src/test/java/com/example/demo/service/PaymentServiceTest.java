package com.example.demo.service;

import com.example.demo.dao.Order.OrderRepository;
import com.example.demo.dao.Payment.PaymentRepository;
import com.example.demo.model.Member.Member;
import com.example.demo.model.Order.Order;
import com.example.demo.model.Order.OrderStatus;
import com.example.demo.model.Payment.Payment;
import com.example.demo.model.Payment.PaymentMethod;
import com.example.demo.model.Payment.PaymentStatus;
import com.example.demo.service.Payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class PaymentServiceTest {

    private final PaymentRepository paymentRepository = Mockito.mock(PaymentRepository.class);
    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);

    private final PaymentService paymentService = new PaymentService(paymentRepository, orderRepository);

    @Test
    void 결제성공() {
        // given
        Member member = new Member("철수", "test@test.com");
        Order order = new Order(member, BigDecimal.valueOf(5000));

        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Mockito.when(paymentRepository.findByOrderId(1L)).thenReturn(Optional.empty());
        Mockito.when(paymentRepository.save(Mockito.any())).thenAnswer(inv -> inv.getArgument(0));

        // when
        Payment payment = paymentService.pay(1L, PaymentMethod.CARD, BigDecimal.valueOf(5000));

        // then
        assertThat(payment.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(order.getStatus()).isEqualTo(OrderStatus.PAID);
    }

    @Test
    void 결제_주문없음_예외() {
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                paymentService.pay(1L, PaymentMethod.CARD, BigDecimal.valueOf(5000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문이 존재하지 않습니다.");
    }
}
