package com.board.board_lsh.service;

import com.board.board_lsh.dto.RelatedBoardDto;
import com.board.board_lsh.entity.Board;
import com.board.board_lsh.entity.Keyword;
import com.board.board_lsh.global.exception.BoardNotFoundException;
import com.board.board_lsh.repository.BoardRepository;
import com.board.board_lsh.repository.KeywordRepository;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.KomoranResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeyWordService {
    private final KeywordRepository keywordRepository;
    private  final BoardRepository boardRepository;


    // 문장을 단어로 쪼갠 후 각 단어 카운트
    public Map<String, Integer> analyzeContent(String content) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        KomoranResult analyzeResultList = komoran.analyze(content);
        List<String> stringList = analyzeResultList.getNouns(); // 문장을 단어
        Map<String, Integer> wordMap = new HashMap<>(); // 단어와 카운트를 저장할 맵

        // 단어를 순회하며 카운트를 증가
        for (String s : stringList) {
            wordMap.put(s, wordMap.getOrDefault(s, 0) + 1);
        }

        log.info(wordMap.toString()); // 분석된 단어와 카운트를 로그로 출력
        return wordMap;
    }

    // 단어 연관도 계산
    public List<RelatedBoardDto> calculateSimilarity (Long boardId){
        final double frequentWordPercentage = 0.6;

        List<Keyword> keywords = keywordRepository.findAllByBoardId(boardId);
        Map<Long, Integer> relatedBoardTotalCnt = new HashMap<>(); // Map<연관 게시글ID, 총 단어 횟수> 단어 종류x 빈도 수 저장
        Map<Long, Integer> relatedBoardCnt = new HashMap<>(); // 단어 가짓 수 계산
        long boardCnt = boardRepository.count();

        for (Keyword keyword : keywords) { // 조회하는 게시글의 단어 모음
            String s = keyword.getWord(); // 단어 선택
            List<Keyword> sList = keywordRepository.findAllByWord(s); // 해당 단어를 가지고 있는 list
            double wordPercentage = (double) sList.size() / boardCnt; // 단어 빈도 퍼센트

            if (wordPercentage < frequentWordPercentage) { // 단어 빈도 퍼센티지 < 자주쓰이는 단어 퍼센티지
                for (Keyword k : sList) {
                    Long relatedId = k.getBoardId();
                    Integer count = k.getCount();

                    if (!boardId.equals(relatedId)) { // 본인 단어 제외
                        relatedBoardCnt.put(relatedId, relatedBoardCnt.getOrDefault(relatedId, 0) + 1);
                        relatedBoardTotalCnt.put(relatedId, relatedBoardTotalCnt.getOrDefault(relatedId, 0) + count);
                    }
                }
            }
        }

        List<RelatedBoardDto> relatedBoards = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : relatedBoardCnt.entrySet()) {
            Board relatedBoard = boardRepository.findById(entry.getKey()).orElseThrow(
                    () -> new BoardNotFoundException("게시글을 찾을 수 없습니다.")
            );

            if (entry.getValue() > 1) { // 연관 단어 갯수가 1개 이상일 경우
                Long relatedBoardTotalCount = relatedBoard.getWordCount();

                // 유사도 계산
                Double relatedWordCnt = Double.valueOf(entry.getValue());
                Double similarity = relatedWordCnt * relatedWordCnt / relatedBoardTotalCount;

                relatedBoards.add(new RelatedBoardDto(relatedBoard, similarity));
            }
        }

        // 연관 게시글 정렬
        relatedBoards = relatedBoards.stream()
                .sorted(Comparator.comparing(RelatedBoardDto::getSimilarity).reversed())
                .collect(Collectors.toList());
        return relatedBoards;
    }
}
