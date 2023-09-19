package xyz.interfacesejong.interfaceapi.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.interfacesejong.interfaceapi.domain.board.domain.Board;
import xyz.interfacesejong.interfaceapi.domain.board.domain.BoardRepository;
import xyz.interfacesejong.interfaceapi.domain.board.dto.BoardDto;
import xyz.interfacesejong.interfaceapi.domain.file.domain.FileRepository;
import xyz.interfacesejong.interfaceapi.domain.file.domain.UploadFile;
import xyz.interfacesejong.interfaceapi.domain.file.service.FileService;
import xyz.interfacesejong.interfaceapi.domain.user.domain.User;
import xyz.interfacesejong.interfaceapi.domain.user.domain.UserRepository;
import xyz.interfacesejong.interfaceapi.global.util.FileUtils;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final FileService fileService;
    private final FileUtils fileUtils;
    private final FileRepository fileRepository;

    //게시물 저장
    @Transactional
    public void save(BoardDto boardDto, List<MultipartFile> multipartFileList) throws Exception {

        Board board = Board.builder()
                .title(boardDto.getTitle())
                .content(boardDto.getContent())
                .writer(userRepository.findById(boardDto.getUserId()).orElseThrow(() -> new NoSuchElementException("해당 사용자가 없습니다."))).build();

        boardRepository.save(board);

        if(!multipartFileList.isEmpty()) {
            List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
            fileService.saveFiles(board, uploadFileList);
        }
    }

    // 전체 게시물 불러오기
    @Transactional
    public List<BoardDto> getAllBoards() {
        List<Board> boardList = boardRepository.findAll();
        List<BoardDto> boardDtoList = new ArrayList<>();
        for(Board board : boardList) {
            boardDtoList.add(new BoardDto(board));
        }
        return boardDtoList;
    }

    // 작성자 id로 게시물 불러오기
    @Transactional
    public List<BoardDto> findByUserId(Long id) {
        List<BoardDto> boardDtoList = new ArrayList<>();
        List<Board> boardList = boardRepository.findAll();

        for(Board board : boardList) {
            if(board.getWriter().getId().equals(id)) {
                boardDtoList.add(new BoardDto(board));
            }
        }

        return boardDtoList;
    }

    // id로 게시물 불러오기
    @Transactional
    public BoardDto findById(Long id) throws EntityNotFoundException {
        BoardDto boardDto = BoardDto.builder()
                .board(boardRepository.findById(id).get()).build();

        return boardDto;
    }

    // 게시물 삭제
    @Transactional
    public void delete(Long id) throws EntityNotFoundException {
        Board board=boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 게시물이 없습니다."));

        boardRepository.delete(board);
    }

    // 게시물 수정
    @Transactional
    public BoardDto update(BoardDto updatedBoardDto, List<MultipartFile> multipartFileList) throws EntityNotFoundException {
        Board board = boardRepository.findById(updatedBoardDto.getId()).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 없습니다."));
        board.update(updatedBoardDto.getTitle(), updatedBoardDto.getContent());

        fileService.deleteFilesByBoardId(updatedBoardDto.getId());
        if(!multipartFileList.isEmpty()) {
            List<UploadFile> uploadFileList = fileUtils.uploadFiles(multipartFileList);
            fileService.saveFiles(board, uploadFileList);
        }

        return new BoardDto(board);
    }

}
