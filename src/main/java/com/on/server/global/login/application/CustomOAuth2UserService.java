package com.on.server.global.login.application;

import com.on.server.domain.user.domain.DispatchedType;
import com.on.server.domain.user.domain.Gender;
import com.on.server.domain.user.domain.User;
import com.on.server.domain.user.domain.UserStatus;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    /*
    private final UserService userService;

    public CustomOAuth2UserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 이메일을 가져오는 부분
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        // 닉네임을 가져오는 부분
        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
        String nickname = (String) properties.get("nickname");

        Optional<User> optionalUser = userService.findByEmail(email);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            user = User.builder()
                    .email(email)
                    .name(nickname)
                    .nickname(nickname)
                    .age(0) // 초기값, 프론트엔드에서 업데이트
                    .gender(Gender.MALE) // 초기값, 프론트엔드에서 업데이트
                    .phone("") // 초기값, 프론트엔드에서 업데이트
                    .isDispatchConfirmed(false)
                    .dispatchedUniversity("")
                    .dispatchedType(DispatchedType.DISPATCHED)
                    .userStatus(UserStatus.TEMPORARY)
                    .country(null) // 초기값, 프론트엔드에서 업데이트
                    .university(null) // 초기값, 프론트엔드에서 업데이트
                    .build();
            userService.save(user);
        }

        return new DefaultOAuth2User(
                Collections.singleton(new OAuth2UserAuthority(attributes)),
                attributes,
                "id"
        );
    }

     */
}