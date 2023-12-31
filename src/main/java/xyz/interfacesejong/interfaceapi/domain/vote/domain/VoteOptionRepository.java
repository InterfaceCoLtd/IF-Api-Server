package xyz.interfacesejong.interfaceapi.domain.vote.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VoteOptionRepository extends JpaRepository<VoteOption, Long> {
    Optional<VoteOption> findById(Long optionId);

    OptionCountMapping findCountById(Long id);
}
