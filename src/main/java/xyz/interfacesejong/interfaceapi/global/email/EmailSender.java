package xyz.interfacesejong.interfaceapi.global.email;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import xyz.interfacesejong.interfaceapi.global.email.dto.AuthEmailResponse;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class EmailSender {
    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final Logger LOGGER = LoggerFactory.getLogger(EmailSender.class);
    public static final String authCode = createCode();

    public AuthEmailResponse sendMessage(String toMail, Map<String, Object> variables, String templateFileName) throws MessagingException {
        MimeMessage message = createMessage(toMail, variables, templateFileName);
        try {
            mailSender.send(message);
        } catch (MailException exception) {
            LOGGER.info("[sendMessage] {}로 메일 전송 실패", toMail);
            exception.printStackTrace();
            return new AuthEmailResponse("MAIL SEND FAIL", toMail);
            //throw new MailSendException("MAIL SEND FAIL");
        }
        LOGGER.info("[sendMessage] {}로 메일 전송 완료", toMail);
        return new AuthEmailResponse("MAIL SEND SUCCESS", toMail);
    }

    private MimeMessage createMessage(String toMail, Map<String, Object> variables, String templateFileName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, toMail);
        message.setFrom("interfacesejong@gmail.com");
        message.setSubject("인증 메일 : 인터페이스 공식 사이트");

        Context context = new Context();
        context.setVariables(variables);
        LOGGER.info("[create Message] auth code {}", variables.get("authCode"));
        String htmlContent = templateEngine.process(templateFileName, context);


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

