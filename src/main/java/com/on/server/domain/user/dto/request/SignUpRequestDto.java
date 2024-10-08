package com.on.server.domain.user.dto.request;

import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignUpRequestDto {

    @Length(min = 5, max = 20, message = "아이디는 5자 이상 20자 이하여야 합니다.")
    private String loginId;

    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Positive(message = "나이는 양수여야 합니다.")
    private Integer age;

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
                .age(age)
                .name(name)
                .gender(gender)
                .phone(phone)
                .isDispatchConfirmed(false)
                .build();
        user.changeRole(role);
        return user;
    }
}