package com.example.demo.dto.Member;

import com.example.demo.model.Member.Member;

import java.time.LocalDateTime;

public class MemberResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;

    public MemberResponse(Long id, String name, String email, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getCreatedAt()
        );
    }

    public Long getId() { return id; }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
