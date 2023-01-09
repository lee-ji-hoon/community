package com.community.web.controller;

import com.community.domain.board.Board;
import com.community.web.dto.BoardForm;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

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

}