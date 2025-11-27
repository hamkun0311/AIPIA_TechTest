package com.example.demo.service;

import com.example.demo.dao.Member.MemberRepository;
import com.example.demo.dao.Order.OrderRepository;
import com.example.demo.model.Member.Member;
import com.example.demo.model.Order.Order;
import com.example.demo.service.Order.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class OrderServiceTest {

    private final OrderRepository orderRepository = Mockito.mock(OrderRepository.class);
    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);

    private final OrderService orderService = new OrderService(orderRepository, memberRepository);

    @Test
    void 주문생성_성공() {
        // given
        Member member = new Member("철수", "test@test.com");
        Mockito.when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        Order savedOrder = new Order(member, BigDecimal.valueOf(10000));
        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(savedOrder);

        // when
        Order result = orderService.createOrder(1L, BigDecimal.valueOf(10000));

        // then
        assertThat(result.getTotalAmount()).isEqualTo("10000");
        assertThat(result.getMember()).isEqualTo(member);
    }

    @Test
    void 주문생성_회원없음_예외() {
        Mockito.when(memberRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.createOrder(1L, BigDecimal.valueOf(10000)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }
}
