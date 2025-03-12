package com.burntoburn.easyshift.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {

    @NotBlank(message = "code는 필수 입력 값입니다.")
    private String code;
}
