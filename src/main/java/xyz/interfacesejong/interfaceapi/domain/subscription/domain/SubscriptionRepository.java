package xyz.interfacesejong.interfaceapi.domain.subscription.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    List<Subscription> findAllByNoticeIsTrue();
    List<Subscription> findAllByScheduleIsTrue();
    List<Subscription> findAllByVoteIsTrue();

    @Query("SELECT s.id FROM Subscription s WHERE " +
            "(:topic = 'vote' AND s.vote = true) OR " +
            "(:topic = 'schedule' AND s.schedule = true) OR " +
            "(:topic = 'notice' AND s.notice = true)")
    List<Long> findIdsByTopic(@Param("topic") String topic);
}
