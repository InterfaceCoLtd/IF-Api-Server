package xyz.interfacesejong.interfaceapi.global.fcm;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.global.fcm.dto.MessageRequest;

import java.util.List;

@RestController
@RequestMapping("api/fcm")
@RequiredArgsConstructor
public class NotificationController {
    private final PushNotificationService pushNotificationService;
    private final Logger LOGGER = LoggerFactory.getLogger(NotificationController.class);
    @PostMapping()
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

    @GetMapping("{userId}/Notification")
    public ResponseEntity<List<MessageRequest>> findRecentNotificationMessage(@PathVariable Long userId){
        List<MessageRequest> response = pushNotificationService.findRecentThreeMonthsNotifications(userId);
        
        LOGGER.info("[findRecentNotificationMessage] 알림 내역 조회");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
