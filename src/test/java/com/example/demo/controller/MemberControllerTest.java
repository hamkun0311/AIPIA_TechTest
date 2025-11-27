package com.example.demo.controller;

import com.example.demo.dto.Member.MemberCreateRequest;
import com.example.demo.dto.Member.MemberResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void 회원_생성_API_성공() throws Exception {
        MemberCreateRequest req = new MemberCreateRequest("홍길동", "hong@example.com");

        mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("hong@example.com")));
    }

    @Test
    void 회원_조회_API_성공() throws Exception {
        // 1) 먼저 회원 생성
        MemberCreateRequest req = new MemberCreateRequest("김철수", "kim@example.com");

        String responseBody = mockMvc.perform(post("/api/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is("kim@example.com")))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // 2) 응답 JSON에서 id 파싱
        MemberResponse created =
                objectMapper.readValue(responseBody, MemberResponse.class);

        Long createdId = created.getId();

        // 3) 그 id로 다시 조회
        mockMvc.perform(get("/api/members/" + createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("김철수")))
                .andExpect(jsonPath("$.email", is("kim@example.com")));
    }

}
