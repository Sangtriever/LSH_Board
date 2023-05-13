package com.board.board_lsh.dto;

import com.board.board_lsh.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<RelatedBoardDto> relatedBoards;

    public BoardResponseDto(Board board, List<RelatedBoardDto> relatedBoards) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createdAt = board.getCreatedAt();
        this.relatedBoards = relatedBoards;
    }
}
