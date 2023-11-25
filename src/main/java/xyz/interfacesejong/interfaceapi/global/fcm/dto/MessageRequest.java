package xyz.interfacesejong.interfaceapi.global.fcm.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import xyz.interfacesejong.interfaceapi.global.fcm.domain.MessageForm;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@Data
public class MessageRequest {
    private Long id;
    private String title;
    private String body;
    private String topic;
    private String contentType;
    private Long contentId;

    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm:ss")
    private LocalDateTime sendDateTime;

    public MessageRequest(MessageForm form){
        this.id = form.getId();
        this.title = form.getTitle();
        this.body = form.getBody();
        this.topic = form.getTopic();
        this.contentType = form.getContentType();
        this.contentId = form.getContentId();
        this.sendDateTime = form.getCreatedDate();
    }

    public MessageRequest(Long id, String title, String body, String topic, String contentType, Long contentId) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.topic = topic;
        this.contentType = contentType;
        this.contentId = contentId;
    }
}
