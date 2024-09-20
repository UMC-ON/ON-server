package com.on.server.domain.user.dto.request;

import com.on.server.domain.user.domain.Gender;
import lombok.Getter;

@Getter
public class FindPWRequestDto {
    private String loginId;
    private String name;
    private String phone;
    private Gender gender;
    private String nickname;
    private Integer age;
}
