package com.community.web.exception;

import com.community.app.util.Script;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandlerController {
    @ExceptionHandler(value = IdNotFoundException.class)
    public @ResponseBody String notExistId(IdNotFoundException e) {
        log.error("IdNotFoundException={}", e);
        return Script.href("/", e.getMessage());
    }
}
