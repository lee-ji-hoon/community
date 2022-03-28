package com.community.domain.chat;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsByRoomHostAndRoomAttender(Account host, Account attender);

    Room findByRoomHostAndRoomAttender(Account host, Account attender);

    List<Room> findByRoomHostOrderByLastSendTimeDesc(Account account);
    List<Room> findByRoomAttenderOrderByLastSendTimeDesc(Account account);

}
