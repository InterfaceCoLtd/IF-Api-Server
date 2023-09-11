package xyz.interfacesejong.interfaceapi.vote.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import xyz.interfacesejong.interfaceapi.user.domain.User;
import xyz.interfacesejong.interfaceapi.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteOption;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteSubject;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteVoter;
import xyz.interfacesejong.interfaceapi.vote.dto.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteSubjectRepository subjectRepository;

    private final VoteOptionRepository optionRepository;

    private final VoteVoterRepository voterRepository;

    private final UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    /*
    * 새로운 투표 주제 등록
    */
    @Transactional
    public String saveVote(VoteDTO voteDTO) {
        VoteSubject voteSubject = VoteSubject.builder()
                .subject(voteDTO.getSubject())
                .build();
        subjectRepository.save(voteSubject);

        for (OptionDTO option : voteDTO.getOptions()) {
            VoteOption voteOption = VoteOption.builder()
                    .voteSubject(voteSubject)
                    .count(0)
                    .option(option.getOption())
                    .build();

            optionRepository.save(voteOption);
        }

        LOGGER.info("[saveVote] 투표 등록");
        return "Registration complete";
    }

    /*
    * 모든 투표 주제 조회
    */
    @Transactional
    public List<SubjectDTO> getAllSubjects() {
        List<SubjectDTO> subjects = subjectRepository.findAll().stream()
                .map(subject -> SubjectDTO.builder()
                        .subject(subject.getSubject())
                        .build())
                .collect(Collectors.toList());

        LOGGER.info("[getAllSubjects] 모든 투표 조회");
        return subjects;
    }

    /*
    * 특정 주제에 대한 옵션 및 현황 조회
    * */
    @Transactional
    public List<OptionDTO> getOptions(Long id) {
        Optional<VoteSubject> optionalSubjects = subjectRepository.findById(id);
        if (optionalSubjects.isPresent()) {
            List<OptionDTO> options = optionalSubjects.get().getVoteOptions().stream()
                    .map(option -> OptionDTO.builder()
                            .option(option.getOption())
                            .count(option.getCount())
                            .build())
                    .collect(Collectors.toList());

            LOGGER.info("[getOptions] " + id + "에 대한 옵션 조회");
            return options;
        } else {
            LOGGER.info("[getOptions] 존재하지 않는 투표 조회");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unregistered Subject");
        }
    }

    /*
    * 투표 등록
    */
    @Transactional
    public void vote(VoterDTO voterDTO) {
        VoteSubject subject = subjectRepository.findById(voterDTO.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("Non Exist Subject"));

        VoteOption option = optionRepository.findById(voterDTO.getOptionId())
                .orElseThrow(() -> new EntityNotFoundException("Non Exist Option"));

        User user = userRepository.findById(voterDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Non Exist User"));

        boolean hasVote = voterRepository.existsByVoteSubjectAndUser(subject, user);
        if (hasVote){
            LOGGER.info("[vote] 이미 투표한 유저");
            throw new EntityExistsException("Already Voted User");
        }

        VoteVoter voter = VoteVoter.builder()
                .voteSubject(subject)
                .voteOption(option)
                .user(user).build();
        voterRepository.save(voter);

        option.addCount();
        optionRepository.save(option);
        
        LOGGER.info("[vote] 투표 등록 성공");
    }
}
