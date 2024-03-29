package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Getter
public class MemberUser extends User {
    private final Member member;

    public MemberUser(Member member) {
        super(
                member.getEmail(), //User 의 username 필드
                member.getPassword(), //User 의 password 필드
                List.of(new SimpleGrantedAuthority(member.getMemberType().name())) // User 의 Authority 필드
        );
        this.member = member;
    }
}
