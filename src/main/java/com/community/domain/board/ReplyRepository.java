package com.community.domain.board;

import com.community.domain.council.Council;
import com.community.domain.market.Market;
import com.community.domain.study.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBoardOrderByUploadTimeDesc(Board board);
    List<Reply> findAllByCouncilOrderByUploadTimeDesc(Council council);
    List<Reply> findAllByMarketOrderByUploadTimeDesc(Market market);

    boolean existsAllByBoard(Board board);

    boolean existsAllByCouncil(Council council);

    Reply findByRid(Long rid);

    List<Reply> findAllByBoard(Board board);

    List<Reply> findAllByMeetings(Meetings meetings);

    List<Reply> findAllByMarket(Market market);

    List<Reply> findAllByCouncil(Council council);

    List<Reply> findTop3ByBoardOrderByUploadTimeDesc(Board board);

    List<Reply> findAllByMeetingsOrderByUploadTimeDesc(Meetings meetings);
}
