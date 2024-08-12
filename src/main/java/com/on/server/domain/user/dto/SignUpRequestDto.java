package com.on.server.domain.user.dto;

import com.on.server.domain.user.domain.DispatchedType;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDto {

    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Length(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String password;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @Positive(message = "나이는 양수여야 합니다.")
    private Integer age;

    @NotBlank(message = "성별은 필수 입력 값입니다.")
    private Gender gender;

    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String phone;

    @NotNull(message = "학교 인증 여부는 필수 입력 값입니다.")
    private Boolean isDispatchConfirmed;

    @NotBlank(message = "학교 이름은 필수 입력 값입니다.")
    private String dispatchedUniversity;

    @NotNull(message = "학교 분류는 필수 입력 값입니다.")
    private DispatchedType dispatchedType;

    @NotBlank(message = "국가는 필수 입력 값입니다.")
    private String country;

    @NotBlank(message = "대학교는 필수 입력 값입니다.")
    private String university;

    public User toEntity(String encodedPassword, List<String> roles) {

        return User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .phone(phone)
                .isDispatchConfirmed(isDispatchConfirmed)
                .dispatchedUniversity(dispatchedUniversity)
                .dispatchedType(dispatchedType)
                .country(country)
                .university(university)
                .roles(roles)
                .build();
    }
}