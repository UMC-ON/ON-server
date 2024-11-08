package com.on.server.global.mail;

import com.on.server.global.common.ResponseCode;
import com.on.server.global.common.exceptions.InternalServerException;
import com.on.server.global.util.StaticValue;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Configuration
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.sender}")
    private static String senderEmail;

    public Integer sendAuthNumMail(String targetMailAddr) {
        Integer number = createNumber();

        MimeMessage message = CreateMail(
                targetMailAddr,
                StaticValue.AUTH_NUMBER_MAIL_SUBJECT,
                StaticValue.AUTH_NUMBER_MAIL_BODY + "<h1>" + number + "</h1>"
        );
        javaMailSender.send(message);

        return number;
    }

    private MimeMessage CreateMail(String targetMailAddr, String mailSubject, String mailBody) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.setFrom(senderEmail);
            message.setRecipients(MimeMessage.RecipientType.TO, targetMailAddr);
            message.setSubject(mailSubject);
            message.setText(mailBody,"UTF-8", "html");
        } catch (MessagingException e) {
            throw new InternalServerException(ResponseCode.INTERNAL_SERVER, "메일 내용 생성에 실패했습니다.");
        }

        return message;
    }

    private Integer createNumber() {
        return (int)(Math.random() * (90000)) + 100000; // (int) Math.random() * (최댓값-최소값+1) + 최소값
    }

}
