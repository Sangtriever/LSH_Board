package com.board.board_lsh.entity;

import com.board.board_lsh.dto.BoardDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity(name = "board")
@Getter
@NoArgsConstructor
public class Board extends BaseTimeEntity{

    // 게시판은 게시글의 ID, 제목, 본문, 생성날짜로 구성되며 제목과 본문은 각각 텍스트 입니다.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;                // id

    @Column(nullable = false)
    private String title;           // 제목

    @Column(nullable = false)
    private String content;         // 본문

    @Column(nullable = false)
    private Long wordCount;
    public Board(BoardDto boardRequestDto, Long wordCount){
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
        this.wordCount = wordCount;
    }
}
