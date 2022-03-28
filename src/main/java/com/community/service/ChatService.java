package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.chat.Chat;
import com.community.domain.chat.ChatRepository;
import com.community.domain.chat.Room;
import com.community.domain.chat.RoomRepository;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.web.dto.ChatForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final MarketRepository marketRepository;
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
    }

}
