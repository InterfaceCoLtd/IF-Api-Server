package xyz.interfacesejong.interfaceapi.domain.vote.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.*;
import xyz.interfacesejong.interfaceapi.domain.vote.service.VoteService;
import xyz.interfacesejong.interfaceapi.global.aop.Timer;

import java.util.List;

@RestController
@RequestMapping("api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    @Timer
    @PostMapping("create")
    ResponseEntity<CreateResponse> create(@RequestBody VoteDTO voteDTO) {
        return new ResponseEntity<>(voteService.save(voteDTO), HttpStatus.CREATED);
    }

    @Timer
    @GetMapping("find/all")
    ResponseEntity<List<SubjectDTO>> findAllSubject() {
        return new ResponseEntity<>(voteService.findAllSubjects(), HttpStatus.OK);
    }

    @Timer
    @GetMapping("find/{id}")
    ResponseEntity<OptionResponse> findOptionById(@PathVariable Long id) {
        return new ResponseEntity<>(voteService.findBySubjectId(id), HttpStatus.OK);
    }

    @Timer
    @PostMapping("register")
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
