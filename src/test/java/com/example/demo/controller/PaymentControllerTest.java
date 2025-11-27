package com.example.demo.controller;

import com.example.demo.dto.Member.MemberCreateRequest;
import com.example.demo.dto.Member.MemberResponse;
import com.example.demo.dto.Order.OrderCreateRequest;
import com.example.demo.dto.Order.OrderResponse;
import com.example.demo.dto.Payment.PaymentCreateRequest;
import com.example.demo.dto.Payment.PaymentResponse;
import com.example.demo.model.Payment.PaymentMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PaymentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 결제_API_전체흐름_성공() throws Exception {
        // 1) 회원 생성
        MemberCreateRequest memberReq = new MemberCreateRequest("결제유저", "pay-user@example.com");

        String memberJson = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        MemberResponse member = objectMapper.readValue(memberJson, MemberResponse.class);
        Long memberId = member.getId();

        // 2) 주문 생성
        OrderCreateRequest orderReq =
                new OrderCreateRequest(memberId, BigDecimal.valueOf(15000));

        String orderJson = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponse order = objectMapper.readValue(orderJson, OrderResponse.class);
        Long orderId = order.getId();

        // 3) 결제 생성
        PaymentCreateRequest paymentReq =
                new PaymentCreateRequest(orderId, PaymentMethod.CARD, BigDecimal.valueOf(15000));

        String paymentJson = mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paymentReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is("COMPLETED")))
                .andExpect(jsonPath("$.method", is("CARD")))
                // 🔽 amount는 jsonPath로 검사하지 않고, 아래에서 DTO로 검사
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4) 응답 JSON을 DTO로 파싱해서 금액 검증
        PaymentResponse createdPayment = objectMapper.readValue(paymentJson, PaymentResponse.class);
        Long paymentId = createdPayment.getId();

        // BigDecimal은 equals로 스케일 차이 때문에 틀어질 수 있으니 compareTo 사용
        assertThat(createdPayment.getAmount())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(15000));

        // 5) 조회 API로 다시 검증
        String getJson = mockMvc.perform(get("/api/payments/" + paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(paymentId.intValue())))
                .andExpect(jsonPath("$.status", is("COMPLETED")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        PaymentResponse fetched = objectMapper.readValue(getJson, PaymentResponse.class);

        assertThat(fetched.getAmount())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(15000));
    }
}
