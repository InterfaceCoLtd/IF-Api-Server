package xyz.interfacesejong.interfaceapi.global.fcm.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageForm extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String body;

    private String topic;

    private String contentType;

    private Long contentId;

    @Builder
    public MessageForm(String title, String body, String topic, String contentType, Long contentId) {
        this.title = title;
        this.body = body;
        this.topic = topic;
        this.contentType = contentType;
        this.contentId = contentId;
    }
}
