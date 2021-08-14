package com.megait.nocoronazone.validator;

import com.megait.nocoronazone.domain.Member;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SignUpFormValidator implements Validator {

    private final MemberRepository repository;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpForm form = (SignUpForm) target;
        Optional<Member> optional = repository.findByEmail(form.getEmail());
        if(optional.isPresent()){
            errors.rejectValue("email", "duplicate.email", "이미 가입된 이메일입니다.");
        }

    }
}
