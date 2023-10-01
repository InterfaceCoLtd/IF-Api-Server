package xyz.interfacesejong.interfaceapi.domain.vote.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.*;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.*;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
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
    public CreateResponse saveVote(SubjectRequest subjectRequest) {
        VoteSubject voteSubject = VoteSubject.builder()
                .subject(subjectRequest.getSubject())
                .startDateTime(subjectRequest.getStartDateTime())
                .endDateTime(subjectRequest.getEndDateTime())
                .build();

        List<VoteOption> options = subjectRequest.getOptions().stream()
                .map(option -> VoteOption.builder()
                        .voteSubject(voteSubject)
                        .option(option.getOption()).build())
                .collect(Collectors.toList());

        CreateResponse createResponse
                = new CreateResponse(subjectRepository.save(voteSubject),
                optionRepository.saveAll(options).stream()
                        .map(option -> OptionDTO.builder()
                                .optionId(option.getId())
                                .option(option.getOption())
                                .count(option.getCount()).build())
                        .collect(Collectors.toList()));

        LOGGER.info("[save] 신규 투표 생성");
        return createResponse;
    }

    /*x`
    * 모든 투표 주제 조회
    */
    @Transactional
    public List<SubjectResponse> findAllSubjects() {
        List<SubjectResponse> subjects = subjectRepository.findAll().stream()
                .map(subject -> SubjectResponse.builder()
                        .subject(subject.getSubject())
                        .subjectId(subject.getId())
                        .startDate(subject.getStartDateTime())
                        .endDate(subject.getEndDateTime())
                        .total(subject.getTotal())
                        .build())
                .collect(Collectors.toList());

        LOGGER.info("[findAllSubjects] 모든 투표 조회");
        return subjects;
    }

    /*
    * 진행 중인 투표 주제 조회
    * */
    @Transactional
    public List<SubjectResponse> findOngoingSubjects(){
        List<SubjectResponse> subjects = subjectRepository.findAllByOngoing(LocalDateTime.now());

        LOGGER.info("[findOngoingSubjects] 활성된 투표 조회");
        return subjects;
    }
    /*
    * 진행 예정 투표 주제 조회
    * */
    @Transactional
    public List<SubjectResponse> findUpcomingSubjects(){
        List<SubjectResponse> subjects = subjectRepository.findAllByUpcoming(LocalDateTime.now());

        LOGGER.info("[findUpcomingSubjects] 예정된 투표 조회");
        return subjects;
    }
    /*
    * 만료된 투표 주제 조회
    * */
    @Transactional
    public List<SubjectResponse> findCompletedSubjects(){
        List<SubjectResponse> subjects = subjectRepository.findAllByCompleted(LocalDateTime.now());

        LOGGER.info("[findCompletedSubjects] 만료된 투표 조회");
        return subjects;
    }



    /*
    * 특정 주제에 대한 옵션 및 현황 조회
    * */
    @Transactional
    public OptionResponse findBySubjectId(Long id) {
        VoteSubject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST SUBJECT"));

        OptionResponse optionResponse = OptionResponse.builder()
                        .subject(subject.getSubject())
                        .endDateTime(subject.getEndDateTime())
                        .options(subject.getVoteOptions().stream()
                                .map(voteOption -> OptionDTO.builder()
                                        .optionId(voteOption.getId())
                                        .option(voteOption.getOption())
                                        .count(voteOption.getCount())
                                        .build())
                                .collect(Collectors.toList()))
                        .build();

        LOGGER.info("[findBySubjectId] {} 조회 투표 ", id);
        return optionResponse;
    }

    /*
    * 투표한 항목 조회
    * */
    @Transactional
    public VoterDTO findVoterBySubjectIdAndUserId(Long subjectId, Long userId){
        if (!voterRepository.findByVoteSubjectIdAndUserId(subjectId, userId).isEmpty()){
            return voterRepository.findByVoteSubjectIdAndUserId(subjectId, userId).get(0);
        } else {
            return null;
        }
    }

    /*
    * 신규 투표자 등록
    */
    @Transactional
    public VoterDTO saveVoter(VoterDTO voterDTO) {
        VoteSubject subject = subjectRepository.findById(voterDTO.getSubjectId())
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST SUBJECT"));

        VoteOption option = optionRepository.findById(voterDTO.getOptionId())
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST OPTION"));

        User user = userRepository.findById(voterDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST USER"));

        boolean hasVote = voterRepository.existsByVoteSubjectAndUser(subject, user);
        if (hasVote){
            LOGGER.info("[vote] 이미 투표한 유저");
            throw new EntityExistsException("ALREADY VOTED USER");
        }

        if (!option.getVoteSubject().getId().equals(subject.getId())){
            LOGGER.info("[vote] 잘못된 참조 관계");
            throw new IllegalArgumentException("INVALID RELATION");
        }

        VoteVoter voter = VoteVoter.builder()
                .voteSubject(subject)
                .voteOption(option)
                .user(user).build();
        voterRepository.save(voter);

        option.increaseCount();
        optionRepository.save(option);

        subject.addTotal();
        subjectRepository.save(subject);
        
        LOGGER.info("[vote] 투표 등록 성공");
        return voterDTO;
    }

    /*
    * 유저 재투표
    * */
    @Transactional
    public VoterDTO updateVoter(VoterUpdateRequest voterUpdateRequest){
        VoteVoter voter = voterRepository.findById(voterUpdateRequest.getVoterId())
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST VOTER"));


        VoteOption updateOption = optionRepository.findById(voterUpdateRequest.getOptionId())
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST OPTION"));

        if (!updateOption.getVoteSubject().getId().equals(voter.getVoteSubject().getId())){
            LOGGER.info("[vote] 잘못된 참조 관계");
            throw new IllegalArgumentException("INVALID RELATION");
        }

        updateOption.increaseCount();

        VoteOption option = voter.getVoteOption();
        option.decreaseCount();

        optionRepository.save(option);
        optionRepository.save(updateOption);

        voter.updateOption(updateOption);
        voterRepository.save(voter);
        LOGGER.info("[updateVoter] voter 업데이트 완료");

        return new VoterDTO(voter.getId(), voter.getVoteSubject().getId(), voter.getVoteOption().getId(), voter.getUser().getId());
    }


    /*
    * 유저 투표 철회
    *  */
    @Transactional
    public void deleteVoter(Long voterId){
        VoteVoter voter = voterRepository.findById(voterId)
                .orElseThrow(() -> new EntityNotFoundException("NON EXIST VOTER"));

        VoteSubject subject = voter.getVoteSubject();
        subject.removeTotal();

        VoteOption option = voter.getVoteOption();
        option.decreaseCount();

        voterRepository.delete(voter);
        LOGGER.info("[deleteVoter] 투표 철회");
    }

    /*
    * 투표 옵션 삭제
    * */
    @Transactional
    public void deleteOptionById(Long optionId){
        if (optionRepository.findCountById(optionId).getCount() != 0){
            LOGGER.info("[deleteOptionById] 투표 내역이 있는 옵션");
            throw new IllegalStateException("ALREADY SOMEONE VOTING THIS OPTION");
        }
        try {
            optionRepository.deleteById(optionId);
        }catch (EmptyResultDataAccessException exception){
            LOGGER.info("[deleteOptionById] 존재하지 않는 id");
            throw new EntityNotFoundException("NON EXIST SCHEDULE");
        }
        LOGGER.info("[deleteOptionById] {} 옵션 삭제", optionId);
    }

    /*
    * 투표 주제 삭제
    * */
    @Transactional
    public void deleteSubjectById(Long subjectId){
        try {
            subjectRepository.deleteById(subjectId);
        }catch (EmptyResultDataAccessException exception){
            LOGGER.info("[deleteSubjectById] 존재하지 않는 id");
            throw new EntityNotFoundException("NON EXIST SCHEDULE");
        }
        LOGGER.info("[deleteSubjectById] {} 주제 삭제", subjectId);
    }

    /*TODO subject, option 수정 기능
    * */


}
