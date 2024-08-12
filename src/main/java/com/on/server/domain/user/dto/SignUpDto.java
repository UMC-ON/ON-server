package com.on.server.domain.user.dto;

import com.on.server.domain.country.Country;
import com.on.server.domain.university.University;
import com.on.server.domain.user.domain.DispatchedType;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String email;
    private String password;
    private String nickname;
    private Integer age;
    private Gender gender;
    private String phone;
    private Boolean isDispatchConfirmed;
    private String dispatchedUniversity;
    private DispatchedType dispatchedType;
    private String country;
    private String university;
    private List<String> roles = new ArrayList<>();

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