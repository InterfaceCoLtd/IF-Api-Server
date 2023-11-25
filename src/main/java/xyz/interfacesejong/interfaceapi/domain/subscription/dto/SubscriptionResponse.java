package xyz.interfacesejong.interfaceapi.domain.subscription.dto;

import lombok.Builder;
import lombok.Data;
import xyz.interfacesejong.interfaceapi.domain.subscription.domain.Subscription;

@Data
public class SubscriptionResponse {
    private Long id;
    private String fcmToken;
    private Boolean notice;
    private Boolean schedule;
    private Boolean vote;
    private Integer badge;

    @Builder
    public SubscriptionResponse(Long id, String fcmToken, Boolean notice, Boolean schedule, Boolean vote, Integer badge) {
        this.id = id;
        this.fcmToken = fcmToken;
        this.notice = notice;
        this.schedule = schedule;
        this.vote = vote;
        this.badge = badge;
    }

    public SubscriptionResponse(Subscription subscription){
        this.id = subscription.getId();
        this.fcmToken = subscription.getFcmToken();
        this.notice = subscription.getNotice();
        this.schedule = subscription.getSchedule();
        this.vote = subscription.getVote();
        this.badge = subscription.getBadge();
    }
}
