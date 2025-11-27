package com.example.demo.controller.Member;

import com.example.demo.dto.Member.MemberCreateRequest;
import com.example.demo.dto.Member.MemberResponse;
import com.example.demo.model.Member.Member;
import com.example.demo.service.Member.MemberService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public MemberResponse register(@Valid @RequestBody MemberCreateRequest request) {
        Member member = memberService.register(request.getName(), request.getEmail());
        return MemberResponse.from(member);
    }

    @GetMapping("/{id}")
    public MemberResponse get(@PathVariable Long id) {
        return MemberResponse.from(memberService.get(id));
    }

    @GetMapping
    public List<MemberResponse> getAll() {
        return memberService.getAll().stream()
                .map(MemberResponse::from)
                .toList();
    }
}
