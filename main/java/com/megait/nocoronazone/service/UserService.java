package com.megait.nocoronazone.service;

import com.megait.nocoronazone.domain.User;
import com.megait.nocoronazone.domain.UserType;
import com.megait.nocoronazone.form.SignUpForm;
import com.megait.nocoronazone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public User processNewUser(SignUpForm signUpForm){

        User user = User.builder()
                .email(signUpForm.getEmail())
                .nickname(signUpForm.getNickname())
                .password(signUpForm.getPassword())
                .userType(UserType.ROLE_USER)
                .build();

        User newUser = userRepository.save(user);

        //TODO - 0811 회원인증 이메일 전송 구현
        return newUser;
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //TODO - 0811 이메일
        return null;
    }
}
