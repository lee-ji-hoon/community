package com.community.domain.chat;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findBySenderOrderBySendTimeDesc(Account sender);

    Optional<Chat> findBySenderAndReceiver(Account sender, Account receiver);

    List<Chat> findByRoom(Long roomId);
}
