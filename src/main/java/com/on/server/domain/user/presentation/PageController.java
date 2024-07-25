package com.on.server.domain.user.presentation;


import com.on.server.global.config.oauth2.KakaoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/login")
public class PageController {

    private final KakaoApi kakaoApi;

    @GetMapping("/page")
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+kakaoApi.getKakaoApiKey()+"&redirect_uri="+kakaoApi.getKakaoRedirectUrl();
        model.addAttribute("location", location);

        return "login";
    }
}
