package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.chat.Chat;
import com.community.domain.chat.ChatRepository;
import com.community.domain.chat.Room;
import com.community.domain.chat.RoomRepository;
import com.community.web.dto.ChatForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final RoomRepository roomRepository;

    public void saveNewRoom(ChatForm chatForm, Account roomHost, Account roomAttender, Account currentUser) {
        Room room = Room.builder()
                .roomHost(roomHost)
                .roomAttender(roomAttender)
                .lastSendMsg(chatForm.getContent())
                .lastSendTime(LocalDateTime.now())
                .build();

        roomRepository.save(room);

        Chat chat = Chat.builder()
                .sender(currentUser)
                .room(room)
                .content(chatForm.getContent())
                .sendTime(LocalDateTime.now())
                .readChk(false)
                .build();

        chatRepository.save(chat);
    }
    public void updateChat(ChatForm chatForm, Account roomHost, Account roomAttender, Account currentUser) {
        Room currentRoom = roomRepository.findByRoomHostAndRoomAttender(roomHost, roomAttender);
        Chat chat = Chat.builder()
                .sender(currentUser)
                .room(currentRoom)
                .content(chatForm.getContent())
                .sendTime(LocalDateTime.now())
                .readChk(false)
                .build();
        chatRepository.save(chat);
        currentRoom.setLastSendMsg(chat.getContent());
        currentRoom.setLastSendTime(LocalDateTime.now());
        roomRepository.save(currentRoom);
    }

    public void existChatUpdate(Long roomId, ChatForm chatForm, Account sender) {
        Room currentRoom = roomRepository.findByRoomId(roomId);
        Chat chat = Chat.builder()
                .sender(sender)
                .room(currentRoom)
                .content(chatForm.getContent())
                .sendTime(LocalDateTime.now())
                .readChk(false)
                .build();

        chatRepository.save(chat);
        currentRoom.setLastSendMsg(chat.getContent());
        currentRoom.setLastSendTime(LocalDateTime.now());
        roomRepository.save(currentRoom);
    }

    public void readCheckService(List<Chat> findChatLists, Account account) {
        for (Chat chat : findChatLists) {
            if (!account.getNickname().equals(chat.getSender().getNickname())) {
                chat.setReadChk(true);
                chatRepository.save(chat);
            }
        }
    }

    // view에서 사용
    public int unReadCount(Room myRoom) {
        int cnt;
        List<Chat> findReadChkFalse = chatRepository.findByRoomAndReadChk(myRoom, false);
        cnt = findReadChkFalse.size();
        return cnt;
    }

    // view에서 사용
    public Map<String, Chat> dateCheckFunction(Room myRoom) {
        // Map<Chat, LocalDateTime>
        Map<Long, String> dateMap = new HashMap<>();
        Map<Chat, LocalDateTime> groupBySendTime = new LinkedHashMap<>();
        // map의 chat id랑 List의 chat id랑 같으면 날짜 출력
        Map<String, Chat> groupBySendDateTime = new HashMap<>();
        List<Chat> chatList = myRoom.getChatList();
        for (Chat chat : chatList) {
            LocalDateTime chatDate = chat.getSendTime();
            String convertChatDate = chatDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if (!groupBySendDateTime.containsKey(convertChatDate)) {
                groupBySendDateTime.put(convertChatDate, chat);
            }
        }
        return groupBySendDateTime;
    }

}
