package com.community.domain.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
public interface BoardRepositoryExtension {

    Page<Board> findByKeywordAndType(String type, String keyword, Pageable pageable);
}
