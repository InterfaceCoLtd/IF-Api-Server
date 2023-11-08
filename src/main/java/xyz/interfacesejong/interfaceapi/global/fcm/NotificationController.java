package xyz.interfacesejong.interfaceapi.global.fcm;

import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.global.fcm.dto.MessageRequest;

@RestController
@RequestMapping("api")
@RequiredArgsConstructor
public class NotificationController {
    private final PushNotificationService pushNotificationService;
    private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    @PostMapping("fcm")
    public ResponseEntity<Void> sendCustomNotification(@RequestParam(value = "userId", required = false) Long userId,
                                                         @RequestParam(value = "topic", required = false) String topic,
                                                         @RequestParam(value = "token", required = false) String token,
                                                         @RequestBody MessageRequest request){
        if (topic.isEmpty() && token.isEmpty() && userId==null){
            LOGGER.info("[] missing field");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        pushNotificationService.sendCustomNotification(request, userId, topic, token);
        LOGGER.info("[sendCustomNotification] send message");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
