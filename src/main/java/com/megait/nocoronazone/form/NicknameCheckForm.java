package com.megait.nocoronazone.form;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter @AllArgsConstructor
public class NicknameCheckForm {
    @Pattern(
            regexp = "^[a-zA-Z가-힣][0-9a-zA-Z가-힣]{4,8}$",
            message = "닉네임은 영어 또는 한글로 시작하는 4자 이상 8자 이하여야합니다."
    )
    private String nickname;
}
