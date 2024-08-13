package com.on.server.domain.user.dto;

import com.on.server.domain.user.domain.DispatchedType;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Set;

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

    private DispatchedType dispatchedType;

    private String dispatchedUniversity;

    private String universityUrl;

    private String country;

    public User toEntity(String encodedPassword, UserStatus role) {
        User user = User.builder()
                .email(email)
                .password(encodedPassword)
                .nickname(nickname)
                .age(age)
                .gender(gender)
                .phone(phone)
                .isDispatchConfirmed(isDispatchConfirmed)
                .dispatchedType(dispatchedType)
                .dispatchedUniversity(dispatchedUniversity)
                .universityUrl(universityUrl)
                .country(country)
                .build();
        user.addRole(role);
        return user;
    }
}