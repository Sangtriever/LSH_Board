package com.board.board_lsh.repository;

import com.board.board_lsh.entity.Keyword;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    List<Keyword> findAllByBoardId(Long boardId);
    List<Keyword> findAllByWord(String word);
}