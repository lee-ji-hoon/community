package com.community.domain.chat;

import com.community.domain.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    Boolean existsByRoomHostAndRoomAttender(Account host, Account attender);

    Room findByRoomHostAndRoomAttender(Account host, Account attender);

    List<Room> findByRoomHostOrderByLastSendTimeDesc(Account account);
    List<Room> findByRoomAttenderOrderByLastSendTimeDesc(Account account);

    List<Room> findByRoomHostOrRoomAttenderOrderByLastSendTimeDesc(Account account1, Account account2);

    List<Room> findTop2ByRoomHostOrderByLastSendTimeDesc(Account account);
    List<Room> findTop2ByRoomAttenderOrderByLastSendTimeDesc(Account account);

    List<Room> findTop4ByRoomAttenderOrRoomAttenderOrderByLastSendTimeDesc(Account account1, Account account2);

    Room findByRoomId(Long roomId);

}
