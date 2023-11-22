package xyz.interfacesejong.interfaceapi.domain.subscription.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByNoticeIsTrue();
    List<Subscription> findAllByScheduleIsTrue();
    List<Subscription> findAllByVoteIsTrue();
}
