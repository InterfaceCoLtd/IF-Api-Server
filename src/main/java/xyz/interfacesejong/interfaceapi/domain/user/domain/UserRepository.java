package xyz.interfacesejong.interfaceapi.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deviceId = null WHERE u.deviceId = :deviceId")
    void updateDeviceIdToNull(@Param("deviceId") UUID deviceID);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.deviceId = :deviceId WHERE u.id = :id")
    void updateDeviceIdByUserId(@Param("id") Long Id, @Param("deviceId") UUID deviceId);

    Optional<User> findByDeviceId(UUID deviceId);

    boolean existsByEmail(String email);
}
