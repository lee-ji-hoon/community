package com.community.service;

import com.community.domain.account.Account;
import com.community.domain.account.AccountRepository;
import com.community.domain.chat.Chat;
import com.community.domain.chat.ChatRepository;
import com.community.domain.market.Market;
import com.community.domain.market.MarketRepository;
import com.community.web.dto.ChatForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final ChatRepository chatRepository;
    private final AccountRepository accountRepository;
    private final MarketRepository marketRepository;

    public void saveMarketChat(ChatForm chatForm, Account account) {
        Optional<Account> accountOptional = accountRepository.findById(chatForm.getReceiver());
        Market market = marketRepository.findByMarketId(chatForm.getMarketId());
        Chat chat = Chat.builder()
                .sender(account)
                .receiver(accountOptional.get())
                .content(chatForm.getContent())
                .sendTime(LocalDateTime.now())
                .market_id(market)
                .build();

        chatRepository.save(chat);

    }

}
