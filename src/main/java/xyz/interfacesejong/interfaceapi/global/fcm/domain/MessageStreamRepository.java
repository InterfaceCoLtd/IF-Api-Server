package xyz.interfacesejong.interfaceapi.global.fcm.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageStreamRepository extends JpaRepository<MessageStream, Long> {
    
}
