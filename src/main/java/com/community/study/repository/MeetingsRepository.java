package com.community.study.repository;

import com.community.study.entity.Meetings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingsRepository extends JpaRepository<Meetings, Long> {
}
