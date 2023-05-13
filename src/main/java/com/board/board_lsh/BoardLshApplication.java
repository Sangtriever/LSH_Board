package com.board.board_lsh;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BoardLshApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardLshApplication.class, args);
    }

}
