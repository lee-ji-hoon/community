package com.community.study.repository;

import com.community.board.entity.Board;
import com.community.study.entity.Meetings;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
public interface MeetingsRepository extends JpaRepository<Meetings, Long> {

    List<Meetings> findFirst9ByOrderByUploadTimeDesc();

    Meetings findByMeetingsId(long id);
}
