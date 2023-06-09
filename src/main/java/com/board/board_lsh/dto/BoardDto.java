package com.board.board_lsh.dto;

import com.board.board_lsh.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BoardDto {
    private String title;
    private String content;

    public BoardDto(Board board) {
        this.title = board.getTitle();
        this.content = board.getContent();
    }
}