package com.example.demo.service;

import com.example.demo.dao.Member.MemberRepository;
import com.example.demo.model.Member.Member;
import com.example.demo.service.Member.MemberService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class MemberServiceTest {

    private final MemberRepository memberRepository = Mockito.mock(MemberRepository.class);
    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    void 회원가입_성공() {
        // given
        Member saved = new Member("오혜성", "ohs@example.com");
        Mockito.when(memberRepository.save(ArgumentMatchers.any())).thenReturn(saved);

        // when
        Member result = memberService.register("오혜성", "ohs@example.com");

        // then
        assertThat(result.getEmail()).isEqualTo("ohs@example.com");
    }

    @Test
    void 회원가입_중복이메일_예외() {
        // given
        Mockito.when(memberRepository.findByEmail("ohs@example.com"))
                .thenReturn(Optional.of(new Member("오혜성", "ohs@example.com")));

        // when & then
        assertThatThrownBy(() -> memberService.register("오혜성", "ohs@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }
}
