package com.board.board_lsh.service;

import com.board.board_lsh.dto.BoardDto;
import com.board.board_lsh.dto.BoardListResponseDto;
import com.board.board_lsh.dto.BoardResponseDto;
import com.board.board_lsh.dto.RelatedBoardDto;
import com.board.board_lsh.entity.Board;
import com.board.board_lsh.entity.Keyword;
import com.board.board_lsh.global.exception.BoardNotFoundException;
import com.board.board_lsh.repository.KeywordRepository;
import com.board.board_lsh.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardService {
    private final KeywordRepository keywordRepository;
    private final BoardRepository boardRepository;
    private final KeyWordService keyWordService;

    public BoardDto createBoard(BoardDto request) {
        String content = request.getContent();
        Map<String, Integer> wordMap = keyWordService.analyzeContent(content); // 내용을 분석하여 단어와 카운트를 Map에 저장
        Long wordCount = (long) wordMap.size(); // 분석된 단어의 총 개수를 계산 -> 나중에 연관도 계산을 위해

        // 게시글 저장
        Board board = new Board(request, wordCount);
        List<Keyword> keywordList = new ArrayList<>();
        // 단어와 카운트 저장
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            String word = entry.getKey();
            Integer count = entry.getValue();
            keywordList.add(keywordRepository.save(new Keyword(board, word, count)));  // 단어와 카운트 저장
        }
        board.setKeywordList(keywordList);
        boardRepository.save(board);
        return request;
    }

    // 게시글 목록 조회
    public List<BoardListResponseDto> getListBoards() {
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        List<BoardListResponseDto> boardResponseDtos = new ArrayList<>();

        for (Board board : boardList) {
            boardResponseDtos.add(new BoardListResponseDto(board));
        }

        return boardResponseDtos;
    }

    // 게시글과 연관된 게시글 조회
    public BoardResponseDto getBoard(Long boardId) {

        Board board = boardRepository.findById(boardId).orElseThrow(
                () -> new BoardNotFoundException("게시글을 찾을 수 없습니다.")
        );

        List<RelatedBoardDto> relatedBoards = keyWordService.calculateSimilarity(boardId);
        return new BoardResponseDto(board, relatedBoards);
    }


}