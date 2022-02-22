package com.community.board.repository;

import com.community.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitleContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByTitleContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByTitleContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);

    List<Board> findByWriterContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByWriterContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByWriterContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);

    List<Board> findByContentContainingOrderByUploadTimeDesc(String keyword);
    List<Board> findByContentContainingAndWriterOrderByUploadTimeDesc(String keyword, String writer);
    List<Board> findByContentContainingAndBoardTitleOrderByUploadTimeDesc(String keyword, String boardTitle);

    List<Board> findAllByWriterIdOrderByUploadTimeDesc(long id);

    Board findByBid(long id);

    List<Board> findAllByUpdatableBoardAndRemovableBoardOrderByUploadTimeDesc(Boolean updatable, Boolean removable);

    List<Board> findAllByBoardTitleOrderByUploadTimeDesc(String boardTitle);

    List<Board> findTop4ByBoardTitleAndUpdatableBoardAndRemovableBoardOrderByUploadTimeDesc(String boardTitle, Boolean updatable, Boolean removable);

    void deleteAllByWriterId(long writerId);
}
