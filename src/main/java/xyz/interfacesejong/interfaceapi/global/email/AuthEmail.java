package xyz.interfacesejong.interfaceapi.global.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class AuthEmail {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final Logger LOGGER = LoggerFactory.getLogger(AuthEmail.class);
    public static final String authCode = createCode();

    public AuthEmailResponse sendMessage(String toMail) throws MessagingException {
        MimeMessage message = createMessage(toMail);
        try {
            mailSender.send(message);
        } catch (MailException exception) {
            throw new MailSendException("MAIL SEND FAIL");
        }
        return new AuthEmailResponse(authCode, toMail);
    }

    private MimeMessage createMessage(String toMail) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, toMail);
        message.setFrom("interfacesejong@gmail.com");
        message.setSubject("인증 코드 : 인터페이스 공식 사이트");

        Context context = new Context();
        context.setVariable("authCode", authCode);
        String htmlContent = templateEngine.process("emailTemplate", context);


        message.setText(htmlContent, "utf-8", "html");
        
        LOGGER.info("[createMessage] 메일 생성");
        return message;
    }

    public static String createCode() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (random.nextInt(3) == 0) {
                stringBuilder.append((char) (random.nextInt(26) + 'A'));
            } else {
                stringBuilder.append((char) (random.nextInt(10) + '0'));
            }
        }
        return stringBuilder.toString();
    }

}

