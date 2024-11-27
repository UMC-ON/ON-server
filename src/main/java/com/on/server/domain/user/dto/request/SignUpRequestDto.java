package com.on.server.domain.user.dto.request;

import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
public class SignUpRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    private String loginId;

    @NotBlank(message = "이메일 인증번호는 필수 입력 값입니다.")
    private Integer signUpAuthNum;

    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Schema(description = "생년월일", example = "1999-01-01")
    @NotNull(message = "생년월일은 필수 입력 값입니다.")
    private LocalDate birth;

    @NotBlank(message = "이름은 필수 입력 값입니다.")
    private String name;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private Gender gender;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    public User toEntity(String encodedPassword, UserStatus role) {
        User user = User.builder()
                .loginId(loginId)
                .password(encodedPassword)
                .nickname(nickname)
                .birth(birth)
                .name(name)
                .gender(gender)
                .phone(phone)
                .isDispatchConfirmed(false)
                .build();
        user.changeRole(role);
        return user;
    }
}