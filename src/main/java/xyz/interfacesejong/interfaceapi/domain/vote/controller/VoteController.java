package xyz.interfacesejong.interfaceapi.domain.vote.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.OptionResponse;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.SubjectDTO;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.VoteDTO;
import xyz.interfacesejong.interfaceapi.domain.vote.dto.VoterDTO;
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
    ResponseEntity<String> registerVote(@RequestBody VoteDTO voteDTO) {
        String ret = voteService.saveVote(voteDTO);

        LOGGER.info("[registerVote] 투표 등록 완료");
        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @Timer
    @GetMapping("find/0")
    ResponseEntity<List<SubjectDTO>> findAllSubject() {
        List<SubjectDTO> subjects = voteService.getAllSubjects();

        LOGGER.info("[getAllSubject] 모든 투표 조회");
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @Timer
    @GetMapping("find/{id}")
    ResponseEntity<OptionResponse> findOptionById(@PathVariable Long id) {
        OptionResponse optionResponse = voteService.getOptions(id);

        LOGGER.info("[getOptionById] " + id + " 투표 조회");
        return new ResponseEntity<>(optionResponse, HttpStatus.OK);
    }

    @Timer
    @PostMapping("register")
    ResponseEntity<String> registerVote(@RequestBody VoterDTO voterDTO) {
        if (voterDTO.getSubjectId() == null ||
                voterDTO.getOptionId() == null ||
                voterDTO.getUserId() == null) {

            LOGGER.info("[vote] 비어있는 필드 존재");
            return new ResponseEntity<>("Missing Field", HttpStatus.BAD_REQUEST);
        } else {
            voteService.vote(voterDTO);

            LOGGER.info("[vote] 투표 등록 완료");
            return new ResponseEntity<>("Suceess", HttpStatus.OK);
        }
    }
}
