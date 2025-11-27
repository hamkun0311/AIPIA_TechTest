package com.example.demo.controller;

import com.example.demo.dto.Member.MemberCreateRequest;
import com.example.demo.dto.Member.MemberResponse;
import com.example.demo.dto.Order.OrderCreateRequest;
import com.example.demo.dto.Order.OrderResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;   // ✅ AssertJ
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 주문생성_API_성공() throws Exception {
        // 1) 회원 생성
        MemberCreateRequest memberReq = new MemberCreateRequest("장인자", "jij@example.com");

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
                new OrderCreateRequest(memberId, BigDecimal.valueOf(10000));

        String orderJson = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId", is(memberId.intValue())))  // memberId는 그대로 jsonPath로 검증
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 3) 응답 DTO로 파싱해서 금액 검증
        OrderResponse createdOrder = objectMapper.readValue(orderJson, OrderResponse.class);

        assertThat(createdOrder.getMemberId()).isEqualTo(memberId);
        assertThat(createdOrder.getTotalAmount())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(10000));
    }

    @Test
    void 주문조회_API_성공() throws Exception {
        // 1) 회원 생성
        MemberCreateRequest memberReq = new MemberCreateRequest("이상만", "lsm@example.com");

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
                new OrderCreateRequest(memberId, BigDecimal.valueOf(20000));

        String createdOrderJson = mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderReq)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        OrderResponse createdOrder = objectMapper.readValue(createdOrderJson, OrderResponse.class);
        Long orderId = createdOrder.getId();

        // 3) 생성된 주문을 조회
        String getJson = mockMvc.perform(get("/api/orders/" + orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(orderId.intValue())))          // id는 jsonPath로
                .andExpect(jsonPath("$.memberId", is(memberId.intValue())))   // memberId도 jsonPath로
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 4) 조회 결과를 DTO로 파싱 후 금액 검증
        OrderResponse fetched = objectMapper.readValue(getJson, OrderResponse.class);

        assertThat(fetched.getMemberId()).isEqualTo(memberId);
        assertThat(fetched.getTotalAmount())
                .usingComparator(BigDecimal::compareTo)
                .isEqualTo(BigDecimal.valueOf(20000));
    }
}
