package xyz.interfacesejong.interfaceapi.domain.vote.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.vote.domain.Status;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.*;
import xyz.interfacesejong.interfaceapi.domain.vote.service.VoteService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.util.List;

@RestController
@RequestMapping("api/votes")
@RequiredArgsConstructor
@Tag(name = "Vote", description = "투표 API")
public class VoteController {
    private final VoteService voteService;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    /*
    * 투표 주제 등록*/
    @Timer
    @PostMapping()
    @Operation(summary = "투표 주제 등록", description = "투표 주제와 기한, 하위 선택지를 등록합니다.")
    ResponseEntity<CreateResponse> createVote(@RequestBody SubjectRequest subjectRequest) {
        return new ResponseEntity<>(voteService.saveVote(subjectRequest), HttpStatus.CREATED);
    }

    /*
    * 모든 투표 조회*/
    @Timer
    @GetMapping()
    @Operation(summary = "투표 주제 전체 조회", description = "모든 투표 주제를 조회합니다.")
    ResponseEntity<List<SubjectDTO>> findAllSubjects() {
        return new ResponseEntity<>(voteService.findAllSubjects(), HttpStatus.OK);
    }

    @Timer
    @GetMapping("subjects")
    @Operation(summary = "투표 상태별 조회", description = "투표를 상태 구분에 따라 조회합니다.\n 현재 'ONGOING'만 구현됨")
    ResponseEntity<List<SubjectDTO>> findSubjectsByStatus(@RequestParam Status status){
        if (status == Status.ONGOING){
            return new ResponseEntity<>(voteService.findOngoingSubjects(), HttpStatus.OK);
        } else if (status == Status.UPCOMING){
            return null;
        } else if (status == Status.COMPLETED){
            return null;
        } else {
            throw new IllegalArgumentException("ILLEGAL STATUS");
        }
    }


    /*
    * 주제 id로 투표 조회*/
    @Timer
    @GetMapping("subject/{subjectId}")
    @Operation(summary = "투표 주제 상세 조회", description = "해당 id의 투표 주제와 선택지 정보를 조회합니다.")
    ResponseEntity<OptionResponse> findBySubjectId(@PathVariable Long subjectId) {
        return new ResponseEntity<>(voteService.findBySubjectId(subjectId), HttpStatus.OK);
    }

    @Timer
    @PostMapping("voter")
    @Operation(summary = "투표자 등록", description = "투표자와 선택을 등록합니다.")
    @ApiResponses()
    ResponseEntity<String> registerVote(@RequestBody VoterDTO voterDTO) {
        if (voterDTO.getSubjectId() == null ||
                voterDTO.getOptionId() == null ||
                voterDTO.getUserId() == null) {

            LOGGER.info("[vote] 비어있는 필드 존재");
            throw new IllegalArgumentException("MISSING FIELD");
        } else {
            voteService.vote(voterDTO);

            LOGGER.info("[vote] 투표 등록 완료");
            return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
        }
    }
}
