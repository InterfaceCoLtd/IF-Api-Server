package xyz.interfacesejong.interfaceapi;

import com.google.firebase.messaging.FirebaseMessagingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import xyz.interfacesejong.interfaceapi.global.fcm.PushNotificationService;

@SpringBootApplication
@EnableJpaAuditing
public class InterfaceApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterfaceApiApplication.class, args);

        PushNotificationService service = new PushNotificationService();

        try {
            service.sendMessage();
        } catch (FirebaseMessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
