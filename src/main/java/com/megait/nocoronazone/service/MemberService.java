package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.domain.MemberType;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Validated
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final EmailService emailService;

    @PostConstruct
    @Profile("local")
    public void createNewMember(){

        Member member = Member.builder()
                .email("admin@test.com")
                .password("qwe123")
                .memberType(MemberType.ROLE_ADMIN)
                .nickname("하하")
                .build();

        Member newMember = memberRepository.save(member);
        //emailService.sendEmail(newMember);
    }

    public Member processNewUser(SignUpForm signUpForm){

        Member member = Member.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword())
                .memberType(MemberType.ROLE_USER)
                .build();

        Member newMember = memberRepository.save(member);

        emailService.sendEmail(newMember);

        return newMember;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByEmail(username);
        if (optional.isEmpty()){
            throw new UsernameNotFoundException(username); // 이거 대신 return null 하면 안된다.
        }

        return new MemberUser(optional.get());
    }



    public void checkNickname(String nickname) {

        Optional<Member> member = memberRepository.findByNickname(nickname);
        if(member.isEmpty()){
            throw new IllegalArgumentException("사용가능한 닉네임");
        }
    }

    public void checkEmailToken(String token, String email) {

        Optional<Member> opt = memberRepository.findByEmail(email);
        if (opt.isEmpty()){
            throw new IllegalArgumentException("이메일 틀림");
        }

        Member member = opt.get();
        if(!member.isValidToken(token)){
            throw new IllegalArgumentException("이메일 틀림");
        }

        member.completeSignup();
    }
}
