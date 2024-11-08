package com.on.server.global.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailService mailService;

    @GetMapping("/test")
    public Integer sendAuthNumMail(
            @RequestHeader String targetMailAddr
    ) {
        return mailService.sendAuthNumMail(targetMailAddr);
    }

}
