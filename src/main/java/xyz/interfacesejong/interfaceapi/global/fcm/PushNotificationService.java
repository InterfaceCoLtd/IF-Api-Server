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
import org.springframework.stereotype.Service;
import xyz.interfacesejong.interfaceapi.global.fcm.dto.MessageRequest;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;
    private String TEMP_TOKEN = "f3SGgCfruUIxjqhbtFEcg4:APA91bFIVm1q9baJDsxvV0GBIU66tcHYLNrnjmjZgAq46AxMNC6cSJxc4c2YgduRbQ8ESyciqevor7KJiBlmOmKUNSZfie7TL4jmnJ6-zq45rVJjzz-Np-m_Tzogiz98YPsblIS3dDJ1";
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
    public void init(){
        try {
            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials
                                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                    ).build();
            if (FirebaseApp.getApps().isEmpty()){
                FirebaseApp.initializeApp(firebaseOptions);
                LOGGER.info("Firebase application initialized");
            }
        } catch (IOException exception){
            LOGGER.info("init {}", exception.getMessage());
            throw new RuntimeException(exception.getMessage());
        }
    }

    public void sendFcmScheduleAddedNotification(Long id, Notification notification){

        Map<String, String> data = new HashMap<>();
        data.put("type","Schedule");
        data.put("scheduleId",id.toString());

        sendTopicMessage(notification, data, "member");
    }
    public void sendFcmVoteAddedNotification(Long id, Notification notification){

        Map<String, String> data = new HashMap<>();
        data.put("type","Vote");
        data.put("voteSubjectId",id.toString());

        sendTopicMessage(notification, data, "member");
    }
    public void sendFcmNoticeAddedNotification(Long id, Notification notification){

        Map<String, String> data = new HashMap<>();
        data.put("type","Notice");
        data.put("boardId",id.toString());

        sendTopicMessage(notification, data, "member");
    }

    public void sendCustomNotification(MessageRequest request, Long userId, String topic, String token){
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        List<String> registrationTokens = new ArrayList<>();
        registrationTokens.add(token);

        if (!topic.isEmpty()){
            Message message = Message.builder()
                    .setTopic(topic)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody()).build())
                    .putData("type", "Custom")
                    .setApnsConfig(apnsConfig)
                    .setAndroidConfig(androidConfig)
                    .build();
            LOGGER.info("[sendCustomNotification] create message {}", new Gson().toJson(message));

            try {
                String response = firebaseMessaging.send(message);
                LOGGER.info("[sendCustomNotification] send message {}", response);
            } catch (FirebaseMessagingException exception){
                LOGGER.info("[sendCustomNotification] exception {}", exception.getMessage());
            }
        }

        if (userId != null){
            MulticastMessage multicastMessage = MulticastMessage.builder()
                    .addAllTokens(registrationTokens)
                    .setNotification(Notification.builder()
                            .setTitle(request.getTitle())
                            .setBody(request.getBody()).build())
                    .putData("type", "Custom")
                    .setApnsConfig(apnsConfig)
                    .setAndroidConfig(androidConfig)
                    .build();

            try {
                BatchResponse batchResponse = firebaseMessaging.sendEachForMulticast(multicastMessage);
                LOGGER.info("[sendCustomNotification] send message {}, {} success", batchResponse.getResponses(), batchResponse.getSuccessCount());
            } catch (FirebaseMessagingException exception){
                LOGGER.info("[sendCustomNotification] exception {}", exception.getMessage());
            }
        }
    }


    private void sendTopicMessage(Notification notification, Map<String, String> data, String topic) {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(notification)
                .putAllData(data)
                .setApnsConfig(apnsConfig)
                .setAndroidConfig(androidConfig)
                .build();
        LOGGER.info("[sendTopicMessage] create message {}", new Gson().toJson(message));

        try {
            String response = firebaseMessaging.send(message);
            LOGGER.info("[sendTopicMessage] send message {}", response);
        } catch (FirebaseMessagingException exception){
            LOGGER.info("[sendTopicMessage] exception {}", exception.getMessage());
        }
    }


}
