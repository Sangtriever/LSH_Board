package com.board.board_lsh.dto;

import com.board.board_lsh.entity.Board;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class BoardListResponseDto {

    private String title;
    private LocalDateTime createdAt;

    public BoardListResponseDto (Board board){
        this.title = board.getTitle();
        this.createdAt = board.getCreatedAt();
    }
}
