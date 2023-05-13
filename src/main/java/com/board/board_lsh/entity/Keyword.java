package com.board.board_lsh.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "keyword")
@Getter
@NoArgsConstructor
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // id
    @Column(nullable = false)
    private String word;           // 단어
    @Column(nullable = false)
    private Long boardId;
    @Column(nullable = false)
    private Integer count;

    public Keyword(Board board, String s, Integer count){
        this.boardId = board.getId();
        this.word = s;
        this.count = count;
    }
}
