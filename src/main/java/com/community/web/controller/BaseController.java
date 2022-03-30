package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.alarm.AlarmRepository;
import com.community.domain.chat.Chat;
import com.community.domain.chat.ChatRepository;
import com.community.domain.chat.Room;
import com.community.domain.chat.RoomRepository;
import com.community.service.AlarmService;
import com.community.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.w3c.dom.events.EventException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class BaseController {

    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final AlarmRepository alarmRepository;
    private final ChatService chatService;
    private final AlarmService alarmService;

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
    public void globalChatNotify(Model model, @CurrentUser Account account) {
        // 현재 계정이 참여중인 Room을 찾는 로직
        List<Room> findMyRooms = new ArrayList<>();
        List<Room> roomHostByAccount = roomRepository.findByRoomHostOrderByLastSendTimeDesc(account);
        List<Room> roomAttenderByAccount = roomRepository.findByRoomAttenderOrderByLastSendTimeDesc(account);

        // 해당 Room의 ChatList를 가져오는 로직
        List<Chat> chatNotifyLists = new ArrayList<>();
        findMyRooms.addAll(roomAttenderByAccount);
        findMyRooms.addAll(roomHostByAccount);

        for (Room findMyRoom : findMyRooms) {
            List<Chat> readChkFalseChat = chatRepository.findByRoomAndReadChk(findMyRoom, false);
            for (Chat chat : readChkFalseChat) {
                if (!account.getNickname().equals(chat.getSender().getNickname())) {
                    chatNotifyLists.add(chat);
                }
            }
        }
        model.addAttribute("g_chatNotify", chatNotifyLists);
        model.addAttribute("g_myRoom", findMyRooms);
    }
}
