package xyz.interfacesejong.interfaceapi.domain.subscription.dto;

import lombok.*;

@Data
public class SubscriptionRequest {
    private String fcmToken;
    private Boolean notice;
    private Boolean schedule;
    private Boolean vote;

    @Builder
    public SubscriptionRequest(Boolean notice, Boolean schedule, Boolean vote) {
        this.notice = notice;
        this.schedule = schedule;
        this.vote = vote;
    }
}
