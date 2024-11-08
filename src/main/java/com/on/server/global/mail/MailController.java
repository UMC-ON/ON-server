package com.on.server.global.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mail")
public class MailController {

    private final MailService mailService;

    @PutMapping("/test")
    public Integer sendAuthNumMail(
            @RequestBody String targetMailAddr
    ) {
        return mailService.sendAuthNumMail(targetMailAddr);
    }

}
