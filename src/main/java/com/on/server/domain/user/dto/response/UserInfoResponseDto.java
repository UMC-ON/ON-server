package com.on.server.domain.user.dto.response;

import com.on.server.domain.user.domain.DispatchType;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import lombok.Builder;

import java.util.Set;

@Builder
public class UserInfoResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private Integer age;
    private Gender gender;
    private String phone;
    private Boolean isDispatchConfirmed;
    private DispatchType dispatchType;
    private String dispatchedUniversity;
    private String universityUrl;
    private String country;
    private UserStatus userStatus;

    public static UserInfoResponseDto from(User user) {
        Set<UserStatus> userStatusSet = user.getRoles();
        if (userStatusSet.size() != 1)
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "User 권한은 하나여야 합니다. 관리자에게 문의 바랍니다. 현재 권한 수: " + userStatusSet.size());
        UserStatus userStatus = userStatusSet.stream().findFirst().get();

        if (user.getIsDispatchConfirmed()) {
            return UserInfoResponseDto.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .nickname(user.getNickname())
                    .age(user.getAge())
                    .gender(user.getGender())
                    .phone(user.getPhone())
                    .isDispatchConfirmed(user.getIsDispatchConfirmed())
                    .dispatchType(user.getDispatchType())
                    .dispatchedUniversity(user.getDispatchedUniversity())
                    .universityUrl(user.getUniversityUrl())
                    .country(user.getCountry())
                    .userStatus(userStatus)
                    .build();
        }
        return UserInfoResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .gender(user.getGender())
                .phone(user.getPhone())
                .isDispatchConfirmed(user.getIsDispatchConfirmed())
                .userStatus(userStatus)
                .build();
        }
}
