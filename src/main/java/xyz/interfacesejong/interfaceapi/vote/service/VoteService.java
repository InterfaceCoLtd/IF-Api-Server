package xyz.interfacesejong.interfaceapi.vote.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteOption;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteSubject;
import xyz.interfacesejong.interfaceapi.vote.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteSubjectRepository subjectRepository;

    private final VoteOptionRepository optionRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    @Transactional
    public String saveVote(VoteDTO voteDTO){
        VoteSubject voteSubject = VoteSubject.builder()
                .subject(voteDTO.getSubject())
                .build();
        subjectRepository.save(voteSubject);

        for (OptionDTO option : voteDTO.getOptions()){
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

    @Transactional
    public List<SubjectDTO> getAllSubjects(){
        List<SubjectDTO> subjects = new ArrayList<>();
        for (VoteSubject subject : subjectRepository.findAll()){
            SubjectDTO subjectDTO = SubjectDTO.builder()
                    .subject(subject.getSubject())
                    .build();

            subjects.add(subjectDTO);
        }

        LOGGER.info("[getAllSubjects] 모든 투표 조회");
        return subjects;
    }

    @Transactional
    public List<OptionDTO> getOptions(Long id){
        Optional<VoteSubject> optionalSubjects = subjectRepository.findById(id);
        if (optionalSubjects.isPresent()){
            return optionalSubjects.get().getVoteOptions().stream()
                    .map(option -> OptionDTO.builder()
                            .option(option.getOption())
                            .build())
                    .collect(Collectors.toList());
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
}
