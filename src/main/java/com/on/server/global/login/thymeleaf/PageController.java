package com.on.server.global.login.thymeleaf;


import com.on.server.global.login.presentation.KakaoApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class PageController {

    @GetMapping("/page")
    public String loginPage(Model model) {
        String api_key = KakaoApi.getKakaoApiKey();
        String redirect_url = KakaoApi.getKakaoRedirectUrl();
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+api_key+"&redirect_uri="+redirect_url;
        model.addAttribute("location", location);

        return "login";
    }
}
