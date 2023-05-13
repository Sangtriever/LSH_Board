package com.board.board_lsh.dto;

import com.board.board_lsh.entity.Board;
import lombok.Getter;

@Getter
public class RelatedBoardDto {
    private Long id;
    private String title;
    private Double similarity;

    public RelatedBoardDto(Board board,Double similarity){
        this.id = board.getId();
        this.title = board.getTitle();
        this.similarity = similarity;
    }
}
