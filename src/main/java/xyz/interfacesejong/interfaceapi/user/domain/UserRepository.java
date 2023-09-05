package xyz.interfacesejong.interfaceapi.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.user.domain.User;

import javax.persistence.Id;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Id> {
    Optional<User> findByEmail(String email);
}
