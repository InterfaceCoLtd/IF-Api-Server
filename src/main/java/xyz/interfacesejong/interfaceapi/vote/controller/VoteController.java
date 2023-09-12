package xyz.interfacesejong.interfaceapi.vote.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.vote.dto.*;
import xyz.interfacesejong.interfaceapi.vote.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    @PostMapping("/register")
    ResponseEntity<String> registerVote(@RequestBody VoteDTO voteDTO) {
        String ret = voteService.saveVote(voteDTO);

        LOGGER.info("[registerVote] 투표 등록 완료");
        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @GetMapping("/0")
    ResponseEntity<List<SubjectDTO>> getAllSubject() {
        List<SubjectDTO> subjects = voteService.getAllSubjects();

        LOGGER.info("[getAllSubject] 모든 투표 조회");
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<OptionResponse> getOptionById(@PathVariable Long id) {
        OptionResponse optionResponse = voteService.getOptions(id);

        LOGGER.info("[getOptionById] " + id + " 투표 조회");
        return new ResponseEntity<>(optionResponse, HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<String> vote(@RequestBody VoterDTO voterDTO) {
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
