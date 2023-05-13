package com.board.board_lsh.controller;

import com.board.board_lsh.dto.BoardDto;
import com.board.board_lsh.dto.BoardResponseDto;
import com.board.board_lsh.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시글 생성 (제목, 내용)
    @PostMapping
    public BoardDto createBoard(@RequestBody BoardDto request){
        return boardService.createBoard(request);
    }

    // 게시글 목록 조회(제목, 날짜 정보)
    @GetMapping
    public List<BoardDto> getListBoards(){
        return boardService.getListBoards();
    }

    // 게시글 조회 (제목, 내용, 연관게시글)
    @GetMapping("/{boardId}")
    public BoardResponseDto getBoard(@PathVariable Long boardId){
        return boardService.getBoard(boardId);
    }
}
