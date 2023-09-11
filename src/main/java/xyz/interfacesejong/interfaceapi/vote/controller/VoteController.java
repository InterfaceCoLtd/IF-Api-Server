package xyz.interfacesejong.interfaceapi.vote.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.interfacesejong.interfaceapi.vote.domain.VoteSubject;
import xyz.interfacesejong.interfaceapi.vote.dto.OptionDTO;
import xyz.interfacesejong.interfaceapi.vote.dto.SubjectDTO;
import xyz.interfacesejong.interfaceapi.vote.dto.VoteDTO;
import xyz.interfacesejong.interfaceapi.vote.service.VoteService;

import java.util.List;

@RestController
@RequestMapping("api/vote")
@RequiredArgsConstructor
public class VoteController {
    private final VoteService voteService;

    private final Logger LOGGER = LoggerFactory.getLogger(VoteController.class);

    @PostMapping("/register")
    ResponseEntity<String> registerVote(@RequestBody VoteDTO voteDTO){
        String ret = voteService.saveVote(voteDTO);

        LOGGER.info("[registerVote] 투표가 등록되었습니다.");
        return new ResponseEntity<>(ret, HttpStatus.CREATED);
    }

    @GetMapping("/0")
    ResponseEntity<List<SubjectDTO>> getAllSubject(){
        List<SubjectDTO> subjects = voteService.getAllSubjects();
        LOGGER.info("[getAllSubject] 모든 투표가 조회되었습니다.");
        return new ResponseEntity<>(subjects, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<List<OptionDTO>> getOptionById(@PathVariable Long id){
        List<OptionDTO> options = voteService.getOptions(id);
        LOGGER.info("[getOptionById] "+id+" 투표 조회");
        return new ResponseEntity<>(options, HttpStatus.OK);
    }
}
