package com.on.server.domain.user.dto.request;

import com.on.server.domain.user.domain.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SendPWAuthNumRequestDto {

    private String loginId;

    private String name;

    private String phone;

    private Gender gender;

}
