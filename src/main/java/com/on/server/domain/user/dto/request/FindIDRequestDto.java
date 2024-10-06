package com.on.server.domain.user.dto.request;

import com.on.server.domain.user.domain.Gender;
import lombok.Getter;

@Getter
public class FindIDRequestDto {
    private String name;
    private String phone;
    private Gender gender;
    private String nickname;
    private Integer age;
}
