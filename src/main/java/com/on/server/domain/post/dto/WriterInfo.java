package com.on.server.domain.post.dto;

import com.on.server.domain.user.domain.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WriterInfo {
    // 작성자 ID
    private Long id;

    // 작성자 닉네임
    private String nickname;

    // 파견국가
    private String country;

    // 파견교
    private String dispatchedUniversity;

    // 작성자 상태
    private UserStatus userStatus;
}
