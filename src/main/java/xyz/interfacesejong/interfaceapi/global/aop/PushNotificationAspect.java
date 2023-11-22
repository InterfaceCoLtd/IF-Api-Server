package xyz.interfacesejong.interfaceapi.global.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xyz.interfacesejong.interfaceapi.domain.Schedule.dto.ScheduleResponse;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardResponse;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.CreateResponse;
import xyz.interfacesejong.interfaceapi.global.fcm.PushNotificationService;
import xyz.interfacesejong.interfaceapi.global.fcm.domain.ContentType;

import java.lang.reflect.Method;

@Aspect
@Component
@RequiredArgsConstructor
public class PushNotificationAspect {
    private final PushNotificationService notificationService;
    private final Logger LOGGER = LoggerFactory.getLogger(PushNotificationAspect.class);

    @Pointcut("execution(* xyz.interfacesejong.interfaceapi..*Service.*(..))")
    public void serviceMethod(){
    }
    @Pointcut("@annotation(xyz.interfacesejong.interfaceapi.global.aop.PushNotification)")
    public void pushNotificationMethod(){
    }

    @AfterReturning(value = "serviceMethod() && pushNotificationMethod()", returning = "response")
    public void sendNotificationAOP(JoinPoint joinPoint, Object response){
        LOGGER.info("[sendNotificationAOP] push 전송 시작");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        PushNotification pushNotification = method.getAnnotation(PushNotification.class);

        ContentType topic = pushNotification.topic();

        if (response instanceof BoardResponse){
            BoardResponse boardResponse = (BoardResponse) response;

            notificationService.sendFcmNoticeAddedNotification(
                    boardResponse.getId(),
                    "신규 공지사항 등록",
                    boardResponse.getTitle(),
                    topic.name()
            );
        } else if (response instanceof ScheduleResponse) {
            ScheduleResponse scheduleResponse = (ScheduleResponse) response;

            notificationService.sendFcmScheduleAddedNotification(
                    scheduleResponse.getId(),
                    "신규 일정 등록",
                    scheduleResponse.getTitle(),
                    topic.name()
            );
        } else if (response instanceof CreateResponse) {
            CreateResponse createResponse = (CreateResponse) response;

            notificationService.sendFcmVoteAddedNotification(
                    createResponse.getSubject().getId(),
                    "신규 투표 등록",
                    createResponse.getSubject().getSubject(),
                    topic.name()
            );
        }
    }
}
