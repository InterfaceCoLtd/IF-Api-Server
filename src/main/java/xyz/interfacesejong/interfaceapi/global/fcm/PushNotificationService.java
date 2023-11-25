package xyz.interfacesejong.interfaceapi.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.*;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.subscription.service.SubscriptionService;
import xyz.interfacesejong.interfaceapi.global.fcm.domain.*;
import xyz.interfacesejong.interfaceapi.global.fcm.dto.MessageRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;

    private final MessageStreamRepository streamRepository;
    private final MessageFormRepository formRepository;
    private final SubscriptionService subscriptionService;
    private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

    private final ApnsConfig apnsConfig = ApnsConfig.builder()
            .putHeader("apns-priority", "10")
            .setAps(Aps.builder()
                    .setBadge(1)
                    .setSound("default")
                    .build())
            .build();
    private final AndroidConfig androidConfig = AndroidConfig.builder()
            .setPriority(AndroidConfig.Priority.HIGH)
            .setNotification(AndroidNotification.builder()
                    .setSound("default")
                    .build())
            .build();

    @PostConstruct
    public void init() {
        try {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                    ).build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(firebaseOptions);
                LOGGER.info("[Firebase] application initialized");
            }
        } catch (IOException exception) {
            LOGGER.info("init {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    // 일정 등록 전송
    @Async
    public void sendFcmScheduleAddedNotification(Long id, String title, String body, String topic) {

        Map<String, String> data = new HashMap<>();
        data.put("contentType", topic);
        data.put("contentId", String.valueOf(id));

        sendTopicMessage(title, body, data, topic);
    }

    // 투표 등록 전송
    @Async
    public void sendFcmVoteAddedNotification(Long id, String title, String body, String topic) {

        Map<String, String> data = new HashMap<>();
        data.put("contentType", topic);
        data.put("contentId", String.valueOf(id));

        sendTopicMessage(title, body, data, topic);
    }

    // 공지 등록 전송
    @Async
    public void sendFcmNoticeAddedNotification(Long id, String title, String body, String topic) {

        Map<String, String> data = new HashMap<>();
        data.put("contentType", topic);
        data.put("contentId", String.valueOf(id));

        sendTopicMessage(title, body, data, topic);
    }

    // 메시지 전송
    private void sendTopicMessage(String title, String body, Map<String, String> data, String topic) {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(Notification.builder()
                        .setTitle(title)
                        .setBody(body).build())
                .putAllData(data)
                .setApnsConfig(apnsConfig) // IOS 설정
                .setAndroidConfig(androidConfig) // AOS 설정
                .build();
        LOGGER.info("[sendTopicMessage] create message {}", new Gson().toJson(message));

        try {
            String response = firebaseMessaging.send(message);
            
            // badge 값 증가
            subscriptionService.increaseBadgeCountForTopic(topic);

            // 메시지 형식 저장
            MessageForm form = saveMessageForm(title, body, data, topic);

            // 알림 전송 내역 저장
            saveMessageStream(form, topic);

            LOGGER.info("[sendTopicMessage] send message {}", response);
        } catch (FirebaseMessagingException exception) {
            LOGGER.info("[sendTopicMessage] exception {}", exception.getMessage());
        }
    }

    //TODO 정확하게 기능을 정리해야함
    public void sendCustomNotification(MessageRequest request, Long userId, String topic, String token) {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        List<String> registrationTokens = new ArrayList<>();
        registrationTokens.add(token);
        if (userId != null) {
            userId = null; // TODO userId token 코드 추가
        }

        if (!topic.isEmpty()) {
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody()).build())
                    .putData("contentType", ContentType.CUSTOM.name())
                    .setApnsConfig(apnsConfig)
                    .setAndroidConfig(androidConfig)
                    .build();
            LOGGER.info("[sendCustomNotification] create message {}", new Gson().toJson(message));

            try {
                String response = firebaseMessaging.send(message);
                LOGGER.info("[sendCustomNotification] send message {}", response);
            } catch (FirebaseMessagingException exception) {
                LOGGER.info("[sendCustomNotification] exception {}", exception.getMessage());
            }
        }

        if (token != null) {
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .addAllTokens(registrationTokens)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody()).build())
                    .putData("contentType", ContentType.CUSTOM.name())
                    .setApnsConfig(apnsConfig)
                    .setAndroidConfig(androidConfig)
                    .build();

            try {
                BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage);
                LOGGER.info("[sendCustomNotification] send message {}, {} success", batchResponse.getResponses(), batchResponse.getSuccessCount());
            } catch (FirebaseMessagingException exception) {
                LOGGER.info("[sendCustomNotification] exception {}", exception.getMessage());
            }
        }
    }


    // 메시지 형식 저장
    public MessageForm saveMessageForm(String title, String body, Map<String, String> data, String topic) {
        MessageForm form = new MessageForm(title, body, topic, data.get("contentType"), Long.parseLong(data.get("contentId")));
        formRepository.save(form);

        return form;
    }

    // 메시지 전송 내역 저장
    @Async
    public void saveMessageStream(MessageForm form, String topic) {
        List<Long> users = subscriptionService.findUserIdsByTopic(topic);

        List<MessageStream> messageStreams = users.stream()
                .map(id -> new MessageStream(id, form))
                .collect(Collectors.toList());

        streamRepository.saveAll(messageStreams);
    }

    @Transactional
    public List<MessageRequest> findRecentThreeMonthsNotifications(Long userId){
        LocalDateTime threeMonthsAgo = LocalDateTime.now().minusMonths(3);
        List<MessageRequest> response = streamRepository
                .findFormsByUserIdAndOlderThanThreeMonths(userId, threeMonthsAgo).stream()
                .map(MessageRequest::new)
                .collect(Collectors.toList());

        LOGGER.info("[findRecentThreeMonthsNotifications] 유저{}의 알림 내역 조회", userId);
        return response;
    }

}
