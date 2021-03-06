package com.community.web.controller;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.account.CurrentUser;
import com.community.domain.chat.Chat;
import com.community.domain.chat.ChatRepository;
import com.community.domain.chat.Room;
import com.community.domain.chat.RoomRepository;
import com.community.service.ChatService;
import com.community.web.dto.ChatForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final AccountRepository accountRepository;
    private final RoomRepository roomRepository;
    private final ChatRepository chatRepository;
    private final ChatService chatService;

    @GetMapping("/chat/lists")
    public String chatLists(@CurrentUser Account account, Model model) {
        List<Room> findRoomHostList = roomRepository.findByRoomHostOrderByLastSendTimeDesc(account);
        List<Room> findRoomAttenderList = roomRepository.findByRoomAttenderOrderByLastSendTimeDesc(account);

        List<Room> myRooms = new ArrayList<>();
        myRooms.addAll(findRoomHostList);
        myRooms.addAll(findRoomAttenderList);

        model.addAttribute("myRooms", myRooms);
        model.addAttribute(account);
        return "chat/chat-detail";
    }

    // 읽음 확인
    @ResponseBody
    @RequestMapping(value = "/chat/readChk")
    public String readCheck(@RequestParam(value = "roomId") Long roomId,
                            @CurrentUser Account account) {
        Room currentRoom = roomRepository.findByRoomId(roomId);
        List<Chat> findChatLists = chatRepository.findByRoomAndReadChk(currentRoom, false);
        chatService.readCheckService(findChatLists, account);

        return "";
    }

    // 전체 읽음 확인
    @ResponseBody
    @RequestMapping(value = "/chat/allReadChk")
    public ResponseEntity allReadCheck(@CurrentUser Account account) {
        List<Room> myRooms = new ArrayList<>();
        List<Room> roomAttender = roomRepository.findByRoomAttenderOrderByLastSendTimeDesc(account);
        List<Room> roomHost = roomRepository.findByRoomHostOrderByLastSendTimeDesc(account);
        myRooms.addAll(roomHost);
        myRooms.addAll(roomAttender);
        log.info("roomSize={}", myRooms.size());
        if (myRooms.size() == 0) {
            return ResponseEntity.badRequest().build();
        }
        for (Room room : myRooms) {
            Optional<Chat> roomChat = chatRepository.findTop1ByRoomAndReadChkOrderBySendTimeDesc(room, false);
            if(!roomChat.get().getSender().getNickname().equals(account.getNickname())) {
                log.info("roomNotChkLog={}", room.getRoomId());
                List<Chat> unReadChat = chatRepository.findByRoomAndReadChk(room, false);
                for (Chat chat : unReadChat) {
                    chat.setReadChk(true);
                    chatRepository.save(chat);
                }
                return ResponseEntity.ok().build();
            }
        }
        for (Room room : myRooms) {
            Optional<Chat> roomChat = chatRepository.findTop1ByRoomAndReadChkOrderBySendTimeDesc(room, false);
            if (roomChat.get().getSender().getNickname().equals(account.getNickname())) {
                log.info("roomChkLog={}", room.getRoomId());
                return ResponseEntity.badRequest().build();
            }
        }
        return ResponseEntity.ok().build();
    }

    // 채팅방에서 쪽지 보내기
    @ResponseBody
    @RequestMapping(value = "/chat/send")
    public String createNewChat(@RequestParam(value = "roomId") Long roomId,
                                @RequestParam(value = "c_content") String c_content,
                                ChatForm chatForm, @CurrentUser Account account) {
        chatForm.setContent(c_content);
        chatService.existChatUpdate(roomId, chatForm, account);


        return "";
    }

    // 장터에서 쪽지 보내기
    @ResponseBody
    @RequestMapping(value = "/chat/new")
    public String sendChat(@RequestParam(value = "c_attender") Long c_attender,
                           @RequestParam(value = "c_content") String c_content,
                           ChatForm chatForm, @CurrentUser Account account) throws IOException {

        Optional<Account> roomAttender = accountRepository.findById(c_attender);
        Optional<Account> roomHost = accountRepository.findById(account.getId());
        Room findAccountEqualHost = roomRepository.findByRoomHostAndRoomAttender(roomHost.get(), roomAttender.get());
        Room findAccountEqualAttender = roomRepository.findByRoomHostAndRoomAttender(roomAttender.get(), roomHost.get());

        /*
        * 보낸 사람 = Host, 받는 사람 = Attender
        * isExistHostEqualHost = 쪽지를 보낸사람이 Host, 받은 사람이 Attender
        * isExistHostEqualAttender = 쪽지를 보낸사람이 Attender, 받은 사람이 Host
        */
        Boolean isExistHostEqualHost = roomRepository.existsByRoomHostAndRoomAttender(roomHost.get(), roomAttender.get());
        Boolean isExistHostEqualAttender = roomRepository.existsByRoomHostAndRoomAttender(roomAttender.get(), roomHost.get());

        // 두 사람이 나눈 쪽지가 아예 없을 경우
        if (!isExistHostEqualHost && !isExistHostEqualAttender) {
            log.info(roomHost.get().getNickname() + "와 " + roomAttender.get().getNickname() + "이 나눈 대화가 없음");
            chatForm.setContent(c_content);
            chatService.saveNewRoom(chatForm, roomHost.get(), roomAttender.get(), account);
        }

        // 두 사람이 나눈 쪽지가 있을 경우
        if (isExistHostEqualHost || isExistHostEqualAttender) {
            log.info(roomHost.get().getNickname() + "와 " + roomAttender.get().getNickname() + "이 나눈 대화가 있음");

            if (findAccountEqualHost != null) {
                chatForm.setContent(c_content);
                chatService.updateChat(chatForm, roomHost.get(), roomAttender.get(), account);
            }
            if (findAccountEqualAttender != null) {
                chatForm.setContent(c_content);
                chatService.updateChat(chatForm, roomAttender.get(), roomHost.get(), account);
            }

        }

        String sendChat = "<div class=\"bg-blue-500 border p-4 relative rounded-md\" uk-alert id=\"isUpdated\">\n" +
                "    <button class=\"uk-alert-close absolute bg-gray-100 bg-opacity-20 m-5 p-0.5 pb-0 right-0 rounded text-gray-200 text-xl top-0\">\n" +
                "        <i class=\"icon-feather-x\"></i>\n" +
                "    </button>\n" +
                "    <h3 class=\"text-lg font-semibold text-white\">알림</h3>\n" +
                "    <p class=\"text-white text-opacity-75\">쪽지를 보냈습니다.</p>\n" +
                "</div>";


        return sendChat;
    }
}
