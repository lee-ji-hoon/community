package com.community.web.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.w3c.dom.events.EventException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class BaseController {
    /*@ExceptionHandler({EventException.class, RuntimeException.class})
    public String eventErrorHandler(EventException exception, Model model) {
        model.addAttribute("message","event error");
        return "error-page";
    }*/

    // 특정 컨트롤러에서 바인딩 또는 검증 설정을 변경하고 싶을 때 사용
    /*@InitBinder
    public void initEventBinder(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
        // EventValidator 속성 추가
        //webDataBinder.addValidators(new EventValidator());
    }*/

    //ModelAttribute 또다른 용번
// @Controller 또는 @ControllerAdvice를 사용한 클래에스 모델 정보를 초기화할 때 사용한다.
    @ModelAttribute()
    public void categories(Model model) {
        Map<Long, String> mapnew = new HashMap<>();
        mapnew.put(1L, "하이1");
        mapnew.put(2L, "하이2");
        mapnew.put(3L, "하이3");

        model.addAttribute("categories", mapnew);
    }
}
