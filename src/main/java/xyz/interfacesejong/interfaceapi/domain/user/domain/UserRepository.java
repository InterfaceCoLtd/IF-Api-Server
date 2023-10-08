package xyz.interfacesejong.interfaceapi.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long userId);

    Optional<User> findByDeviceId(byte[] deviceId);

    boolean existsByEmail(String email);
}
