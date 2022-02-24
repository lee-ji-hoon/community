package com.community.report.repository;

import com.community.report.entity.BoardReport;
import com.community.report.entity.ReplyReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyReportRepository extends JpaRepository<ReplyReport, Long> {

}
