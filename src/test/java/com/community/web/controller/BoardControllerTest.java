package com.community.web.controller;

import com.community.domain.board.Board;
import com.community.domain.board.BoardSort;
import com.community.web.dto.BoardForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BoardControllerTest {

    @Autowired
    ModelMapper mapper;

    @Test
    @DisplayName("HashMap이 ModelMapper에 잘 들어오는지 확인")
    void modelMapperTest() {
        Map<String, Object> testMap = new HashMap<>();
        testMap.put("title", "제목테스트");
        testMap.put("content", "내용테스트");

        BoardForm dto = mapper.map(testMap, BoardForm.class);

        System.out.println("dto = " + dto.getTitle());
        System.out.println("dto = " + dto.getContent());

        Board savedBoard = mapper.map(dto, Board.class);

        System.out.println("savedBoard = " + savedBoard.getTitle());
        System.out.println("savedBoard = " + savedBoard.getContent());

        assertThat(dto.getTitle()).isEqualTo("제목테스트");
        assertThat(dto.getContent()).isEqualTo("내용테스트");

        assertThat(savedBoard.getTitle()).isEqualTo("제목테스트");
        assertThat(savedBoard.getContent()).isEqualTo("내용테스트");

    }

    @Test
    @DisplayName("검색 분야가 ENUM 타입에 맞는지 확인")
    void searchTypeMatchTest() throws Exception {

        String type = "FREE";

        String result = BoardSort.valueOf(type).getValue();

        assertThat(result).isEqualTo("자유");

    }

}