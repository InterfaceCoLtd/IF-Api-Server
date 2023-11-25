package xyz.interfacesejong.interfaceapi.global.fcm.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageStreamRepository extends JpaRepository<MessageStream, Long> {
    @Query("SELECT ms.form FROM MessageStream ms WHERE ms.userId = :userId AND ms.createdDate >= :thresholdDate")
    List<MessageForm> findFormsByUserIdAndOlderThanThreeMonths(@Param("userId") Long userId, @Param("thresholdDate") LocalDateTime thresholdDate);
    
}
