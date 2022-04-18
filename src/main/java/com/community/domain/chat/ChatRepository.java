package com.community.domain.chat;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    List<Chat> findByRoom(Room room);

    List<Chat> findByRoomAndReadChk(Room room, Boolean isRead);

    Optional<Chat> findTop1ByRoomAndReadChkOrderBySendTimeDesc(Room room, Boolean isRead);

}
