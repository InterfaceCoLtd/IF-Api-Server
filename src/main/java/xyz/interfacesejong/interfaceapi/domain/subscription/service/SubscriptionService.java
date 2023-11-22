package xyz.interfacesejong.interfaceapi.domain.subscription.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.subscription.domain.Subscription;
import xyz.interfacesejong.interfaceapi.domain.subscription.domain.SubscriptionRepository;
import xyz.interfacesejong.interfaceapi.domain.subscription.dto.SubscriptionRequest;
import xyz.interfacesejong.interfaceapi.domain.subscription.dto.SubscriptionResponse;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.fcm.domain.ContentType;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(SubscriptionService.class);

    /* 비어 있는 레코드 생성
     * User 회원 가입시 후속으로 호출
     * */
    @Transactional
    @Async
    public void createSubscriptionRecord(User user){
        Subscription subscription = new Subscription(user);
        subscriptionRepository.save(subscription);
        LOGGER.info("[CreateSubscriptionRecord] 유저{} 구독 테이블 생성", user.getId());
    }

    // fcm 토큰 변경
    @Transactional
    public SubscriptionResponse updateFcmToken(Long id, SubscriptionRequest request){
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateFcmToken] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        subscription.updateFcmToken(request.getFcmToken());
        subscription = subscriptionRepository.save(subscription);

        LOGGER.info("사용자{} fcm token 갱신", id);

        return new SubscriptionResponse(subscription);
    }

    // notice 구독 상태 변경
    @Transactional
    public SubscriptionResponse updateNoticeStatus(Long id, SubscriptionRequest request){
        if (request.getNotice() == null){
            LOGGER.info("[updateNoticeStatus] 입력 인자가 null");
            throw new IllegalArgumentException("MISSING FIELD");
        }

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateFcmToken] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });


        if (request.getNotice()){
            subscription.subscribeNotice();
            LOGGER.info("사용자{} 공지 구독", id);
        }else {
            subscription.unsubscribeNotice();
            LOGGER.info("사용자{} 공지 구독 취소", id);
        }

        subscription = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(subscription);
    }

    // schedule 구독 상태 변경
    @Transactional
    public SubscriptionResponse updateScheduleStatus(Long id, SubscriptionRequest request){
        if (request.getSchedule() == null){
            LOGGER.info("[updateScheduleStatus] 입력 인자가 null");
            throw new IllegalArgumentException("MISSING FIELD");
        }

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateScheduleStatus] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });


        if (request.getSchedule()){
            subscription.subscribeSchedule();
            LOGGER.info("사용자{} 일정 구독", id);
        }else {
            subscription.unsubscribeSchedule();
            LOGGER.info("사용자{} 일정 구독 취소", id);
        }

        subscription = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(subscription);
    }

    // vote 구독 상태 변경
    @Transactional
    public SubscriptionResponse updateVoteStatus(Long id, SubscriptionRequest request){
        if (request.getVote() == null){
            LOGGER.info("[updateVoteStatus] 입력 인자가 null");
            throw new IllegalArgumentException("MISSING FIELD");
        }

        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateVoteStatus] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });


        if (request.getVote()){
            subscription.subscribeVote();
            LOGGER.info("사용자{} 투표 구독", id);
        }else {
            subscription.unsubscribeVote();
            LOGGER.info("사용자{} 투표 구독 취소", id);
        }

        subscription = subscriptionRepository.save(subscription);

        return new SubscriptionResponse(subscription);
    }


    // 특정 topic 구독중인 레코드 badge 값 +1
    // fcm에서 pushNotification이 실행 될 때 같이 badge 카운트를 늘리는 기능을 한다.
    // 시간상의 이유로 Async로 비동기 선언을 해두었다.
    @Transactional
    @Async
    public void increaseBadgeCountForTopic(String topic){
        List<Subscription> subscriptions = new ArrayList<>();

        if (topic.equals(ContentType.NOTICE.name())){
            subscriptions = subscriptionRepository.findAllByNoticeIsTrue();
        } else if (topic.equals(ContentType.SCHEDULE.name())) {
            subscriptions = subscriptionRepository.findAllByScheduleIsTrue();
        } else if (topic.equals(ContentType.VOTE.name())) {
            subscriptions = subscriptionRepository.findAllByVoteIsTrue();
        }
        subscriptions.forEach(Subscription::increaseBadgeCount);

        LOGGER.info("[increaseBadgeCountForTopic] Topic: {} 구독자 badge 증가", topic);
        subscriptionRepository.saveAll(subscriptions);
    }
    
    // 메시지 읽으면 badge 0으로 초기화
    @Transactional
    public void resetBadgeCount(Long id){
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateVoteStatus] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        subscription.resetBadgeCount();
        LOGGER.info("[resetBadgeCount] 유저{} 알림 모두 읽음 확인", id);
    }

    // 특정 userId 구독중인 Topic 정보 반환
    @Transactional
    public SubscriptionResponse getSubscriptionByUserId(Long id){
        Subscription subscription = subscriptionRepository.findById(id)
                .orElseThrow(()->{
                    LOGGER.info("[updateVoteStatus] 등록되지 않은 userId");
                    return new EntityNotFoundException("NON EXISTS USER");
                });

        SubscriptionResponse response = new SubscriptionResponse(subscription);
        LOGGER.info("[getBadgeCountByUserId] 유저{} 의 구독 정보 확인", id);
        return response;
    }
}
