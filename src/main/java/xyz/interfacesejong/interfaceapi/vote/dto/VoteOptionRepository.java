package xyz.interfacesejong.interfaceapi.vote.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteOption;

import javax.persistence.Id;
import java.util.Optional;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    Optional<VoteOption> findById(Long optionId);
}
