package xyz.interfacesejong.interfaceapi.domain.subscription.domain;

import lombok.Builder;
import lombok.Getter;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.global.util.BaseTime;

import javax.persistence.*;

@Entity
@Getter
public class Subscription extends BaseTime {
    @Id
    Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    private String fcmToken;

    private Boolean notice = true;
    private Boolean schedule = true;
    private Boolean vote = true;

    private Integer badge = 0;

    public void updateFcmToken(String fcmToken){
        this.fcmToken = fcmToken;
    }

    public void increaseBadgeCount(){
        this.badge++;
    }

    public void resetBadgeCount(){
        this.badge = 0;
    }

    public void subscribeNotice(){
        this.notice = true;
    }
    public void subscribeSchedule(){
        this.schedule = true;
    }
    public void subscribeVote(){
        this.vote = true;
    }
    public void unsubscribeNotice(){
        this.notice = false;
    }
    public void unsubscribeSchedule(){
        this.schedule = true;
    }
    public void unsubscribeVote(){
        this.vote = true;
    }

    @Builder
    public Subscription(User user, String fcmToken, Boolean notice, Boolean schedule, Boolean vote, Integer badge) {
        this.user = user;
        this.fcmToken = fcmToken;
        this.notice = notice;
        this.schedule = schedule;
        this.vote = vote;
        this.badge = badge;
    }

    public Subscription(User user) {
        this.user = user;
    }

    public Subscription() {

    }
}
