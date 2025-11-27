package com.example.demo.dto.Member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class MemberCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    @Email
    private String email;

    public MemberCreateRequest() {
    }

    public MemberCreateRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }

    public String getEmail() { return email; }
}
