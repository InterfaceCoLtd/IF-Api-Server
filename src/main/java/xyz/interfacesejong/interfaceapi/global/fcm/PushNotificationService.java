package xyz.interfacesejong.interfaceapi.global.fcm;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PushNotificationService {
    @Value("${fcm.key.path}")
    private String FCM_PRIVATE_KEY_PATH;
    private String TEMP_TOKEN = "fdU05p97pQFT5nMgqxoIKE:APA91bFnyJ-20ncrAC-jAqi1SQxVb1s-fyil-885EkvEvXndwiJk0drI-HaD91PgTCc-bxzfMtv0KwytMY84pqNXLXfWIFlqgJPM2EkULCwEvCOO0v_wBEJ_k8nd8rTDzkh10SgXsb9n";
    private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationService.class);

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

    public void sendMessage() throws FirebaseMessagingException {
        FirebaseMessaging firebaseMessaging = FirebaseMessaging.getInstance();

        Message message = Message.builder()
                .setToken(TEMP_TOKEN)
                .setNotification(Notification.builder()
                        .setTitle("title")
                        .setBody("body").build())
                .build();

        firebaseMessaging.send(message);
    }
}
